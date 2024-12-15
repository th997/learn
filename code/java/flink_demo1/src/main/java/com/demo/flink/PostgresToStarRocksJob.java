package com.demo.flink;

import com.demo.flink.converter.PostgresCustomConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.th997.dbcompare.SqlGenerator;
import com.github.th997.dbcompare.TableColumn;
import com.github.th997.dbcompare.TableQuery;
import com.github.th997.dbcompare.TypeConverter;
import com.github.th997.dbcompare.converter.PostgresToStarRocksConverter;
import com.github.th997.dbcompare.generator.StarRocksGenerator;
import com.github.th997.dbcompare.query.MysqlTableQuery;
import com.github.th997.dbcompare.query.PostgresTableQuery;
import com.starrocks.connector.flink.table.data.DefaultStarRocksRowData;
import com.starrocks.connector.flink.table.sink.SinkFunctionFactory;
import com.starrocks.connector.flink.table.sink.StarRocksSinkOptions;
import com.ververica.cdc.connectors.postgres.source.PostgresSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.shaded.guava30.com.google.common.base.CaseFormat;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostgresToStarRocksJob {
    static Logger log = org.slf4j.LoggerFactory.getLogger(PostgresToStarRocksJob.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static TableQuery pgTableQuery = new PostgresTableQuery();
    private static TableQuery srTableQuery = new MysqlTableQuery();
    private static TypeConverter typeConverter = new PostgresToStarRocksConverter();
    private static SqlGenerator sqlGenerator = new StarRocksGenerator();
    private static Map<String, String> schemaMap = new ConcurrentHashMap<>();
    private static DataSource srDataSource;
    private static DataSource pgDataSource;
    private static AtomicLong count = new AtomicLong();


    public static void main(String[] args) throws Exception {
        log.info("{} start...", PostgresToStarRocksJob.class.getSimpleName());
        // read config
        String jobEnv = ParameterTool.fromArgs(args).get("env", "local");
        String configName = String.format("config-%s.properties", jobEnv);
        InputStream in = PostgresToStarRocksJob.class.getClassLoader().getResourceAsStream(configName);
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(in);
        // create the execution environment
        StreamExecutionEnvironment env;
        if ("local".equals(jobEnv)) {
            env = StreamExecutionEnvironment.createLocalEnvironment();
            env.getCheckpointConfig().setCheckpointStorage(Path.fromLocalFile(new File("/d/tmp/flink-ck")));
        } else {
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.enableCheckpointing(parameterTool.getInt("checkpointingInterval", 10000), CheckpointingMode.AT_LEAST_ONCE);
        String jobName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, PostgresToStarRocksJob.class.getSimpleName()) + "_" + jobEnv;
        // json serialization
        ObjectMapper om = new ObjectMapper();
        Properties dzProperties = new Properties();
        dzProperties.put("decimal.handling.mode", "double");
        dzProperties.put("binary.handling.mode", "bytes");
        dzProperties.put("converters", "datetime");
        dzProperties.put("datetime.type", PostgresCustomConverter.class.getName());
        dzProperties.put("datetime.format", DATE_FORMAT);
        JsonDebeziumDeserializationSchema deserializer = new JsonDebeziumDeserializationSchema(Boolean.TRUE);
        // pg source
        String host = parameterTool.get("pg.host");
        Integer port = new Integer(parameterTool.getInt("pg.port"));
        String username = parameterTool.get("pg.username");
        String password = parameterTool.get("pg.password");
        String database = parameterTool.get("pg.database");
        String schemaList = parameterTool.get("pg.schemaList");
        String tableList = parameterTool.get("pg.tableList");
        String dbMaps = parameterTool.get("pg.database.map");
        final Map<String, String> dbMap;
        if (dbMaps != null && !dbMaps.isEmpty()) {
            dbMap = om.readValue(dbMaps, new TypeReference<Map<String, String>>() {
            });
        } else {
            dbMap = new HashMap<>(0);
        }
        PostgresSourceBuilder<String> pgSourceBuilder = PostgresSourceBuilder.PostgresIncrementalSource.<String>builder() //
                .hostname(host).port(port).database(database).username(username).password(password) //
                .slotName(jobName) //
                .decodingPluginName("pgoutput") // pg default
                .deserializer(deserializer) //
                .debeziumProperties(dzProperties) //
                .splitSize(parameterTool.getInt("pg.splitSize", 100000)) // the split size of each snapshot split
                .distributionFactorUpper(10);// for not discontinuous primary keys
        if (schemaList != null && schemaList.length() > 0) {
            pgSourceBuilder.schemaList(schemaList.split(","));
        }
        if (tableList != null && tableList.length() > 0) {
            pgSourceBuilder.tableList(tableList.split(","));
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", host, port, database));
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(2);
        config.setConnectionTimeout(3000);
        config.addDataSourceProperty("socketTimeout", new Integer(36000));
        pgDataSource = new HikariDataSource(config);
        // starrocks sink
        String srJdbcUrl = parameterTool.get("sr.jdbc-url");
        String srUsername = parameterTool.get("sr.username");
        String srPassword = parameterTool.get("sr.password");
        StarRocksSinkOptions srOptions = StarRocksSinkOptions.builder() //
                .withProperty("jdbc-url", srJdbcUrl)//
                .withProperty("load-url", parameterTool.get("sr.load-url"))//
                .withProperty("database-name", parameterTool.get("sr.database-name"))//
                .withProperty("table-name", "xxx") // not use , but not empty
                .withProperty("username", srUsername)//
                .withProperty("password", srPassword)//
                .withProperty("sink.properties.format", "json")//
                .withProperty("sink.properties.strict_mode", "true")//
                .build();
        int replicationNum = parameterTool.getInt("sr.replicationNum", 1);
        if (replicationNum > 1) {
            sqlGenerator = new StarRocksGenerator(1, replicationNum);
        }
        srOptions.enableUpsertDelete();
        HikariConfig srConfig = new HikariConfig();
        srConfig.setJdbcUrl(srJdbcUrl);
        srConfig.setUsername(srUsername);
        srConfig.setPassword(srPassword);
        srConfig.setMaximumPoolSize(2);
        srConfig.setConnectionTimeout(3000);
        srConfig.addDataSourceProperty("socketTimeout", new Integer(36000));
        srDataSource = new HikariDataSource(srConfig);
        // stream data
        long start = System.currentTimeMillis();
        env.fromSource(pgSourceBuilder.build(), WatermarkStrategy.noWatermarks(), "pg_source").setParallelism(1)//
                .map(data -> {
                    // https://debezium.io/documentation/reference/stable/connectors/postgresql.html#postgresql-events
                    JsonNode json = om.readTree(data);
                    JsonNode payload = json.get("payload");
                    JsonNode source = payload.get("source");
                    String schema = source.get("schema").asText();
                    String table = source.get("table").asText();
                    String key = schema + "." + table;
                    String schemaJsonOld = schemaMap.get(key);
                    String schemaJson = json.get("schema").toString();
                    String targetSchema = dbMap.getOrDefault(schema, schema);
                    if (!Objects.equals(schemaJsonOld, schemaJson)) {
                        compareSchema(schema, table, targetSchema);
                        schemaMap.put(key, schemaJson);
                    }
                    DefaultStarRocksRowData row = new DefaultStarRocksRowData();
                    row.setDatabase(targetSchema);
                    row.setTable(table);
                    String op = payload.get("op").asText();
                    if ("d".equals(op)) {
                        JsonNode before = payload.get("before");
                        ((ObjectNode) before).put("__op", 1);// delete tag
                        row.setRow(before.toString());
                    } else {
                        JsonNode after = payload.get("after");
                        row.setRow(after.toString());
                    }
                    long co = count.addAndGet(1);
                    if (co % 10000 == 0) {
                        log.info("count={},time={}", co, System.currentTimeMillis() - start);
                    }
                    return row;
                }).setParallelism(1).keyBy(row -> row.getDatabase() + "." + row.getTable()) // key by table
                .addSink(SinkFunctionFactory.createSinkFunction(srOptions)).setParallelism(4).name("sr_sink");
        // .addSink(new DiscardingSink()).setParallelism(4).name("sr_sink");
        // start
        env.execute(jobName);
        log.info("{} started", PostgresToStarRocksJob.class.getSimpleName());
    }

    private synchronized static void compareSchema(String srcSchema, String srTable, String targetSchema) throws SQLException {
        log.info("compareSchema start");
        long start = System.currentTimeMillis();
        try (Connection pgConn = pgDataSource.getConnection(); Connection srConn = srDataSource.getConnection()) {
            List<TableColumn> pgColumnList = pgTableQuery.queryTableColumn(pgConn, srcSchema, srTable);
            List<TableColumn> srColumnList = srTableQuery.queryTableColumn(srConn, targetSchema, srTable);
            List<TableColumn> convertColumnList = typeConverter.convert(pgColumnList);
            String tableSql = sqlGenerator.generateTableSql(targetSchema, srTable, srColumnList, convertColumnList);
            if (tableSql != null) {
                log.info("compareSchema tableSql={}", tableSql);
                if (srColumnList == null || srColumnList.isEmpty()) {
                    try (Statement st = srConn.createStatement()) {
                        st.execute(String.format("create database if not exists `%s`", targetSchema));
                    }
                }
                try (Statement st = srConn.createStatement()) {
                    st.execute(tableSql);
                }
                waitSrAltering(targetSchema);
            }
        }
        log.info("compareSchema end, time={}", System.currentTimeMillis() - start);
    }

    private static void waitSrAltering(String schema) throws SQLException {
        String sql = String.format("show alter table column from %s order by createtime desc limit 100", schema);
        while (true) {
            log.info("waitSrAltering running");
            boolean isBreak = true;
            try (Connection srConn = srDataSource.getConnection()) {
                try (Statement st = srConn.createStatement()) {
                    try (ResultSet rs = st.executeQuery(sql)) {
                        while (rs.next()) {
                            if ("!FINISHED".equals(rs.getString("State"))) {
                                isBreak = false;
                            }
                        }
                    }
                }
            }
            if (isBreak) {
                log.info("waitSrAltering end");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
