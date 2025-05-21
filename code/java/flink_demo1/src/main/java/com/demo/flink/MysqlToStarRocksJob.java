package com.demo.flink;

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
import com.ververica.cdc.connectors.mysql.source.MySqlSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.shaded.guava30.com.google.common.base.CaseFormat;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MysqlToStarRocksJob {
    static Logger log = org.slf4j.LoggerFactory.getLogger(MysqlToStarRocksJob.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static TableQuery pgTableQuery = new PostgresTableQuery();
    private static TableQuery srTableQuery = new MysqlTableQuery();
    private static TypeConverter typeConverter = new PostgresToStarRocksConverter();
    private static SqlGenerator sqlGenerator = new StarRocksGenerator();
    private static Map<String, String> schemaMap = new ConcurrentHashMap<>();
    private static AtomicLong count = new AtomicLong();

    public static void main(String[] args) throws Exception {
        log.info("{} start...", MysqlToStarRocksJob.class.getSimpleName());
        // read config
        String jobEnv = ParameterTool.fromArgs(args).get("env", "local");
        String configName = String.format("config-%s.properties", jobEnv);
        InputStream in = MysqlToStarRocksJob.class.getClassLoader().getResourceAsStream(configName);
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
        String jobName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, MysqlToStarRocksJob.class.getSimpleName()) + "_" + jobEnv;
        // json serialization
        ObjectMapper om = new ObjectMapper();
        Properties dzProperties = new Properties();
        dzProperties.put("decimal.handling.mode", "double");
        dzProperties.put("converters", "datetime");
        dzProperties.put("datetime.format", DATE_FORMAT);
        JsonDebeziumDeserializationSchema deserializer = new JsonDebeziumDeserializationSchema(Boolean.TRUE);
        // pg source
        String host = parameterTool.get("mysql.host");
        Integer port = new Integer(parameterTool.getInt("mysql.port"));
        String username = parameterTool.get("mysql.username");
        String password = parameterTool.get("mysql.password");
        String databaseList = parameterTool.get("mysql.databaseList");
        String tableList = parameterTool.get("mysql.tableList");
        String dbMaps = parameterTool.get("mysql.database.map");
        final Map<String, String> dbMap;
        if (dbMaps != null && !dbMaps.isEmpty()) {
            dbMap = om.readValue(dbMaps, new TypeReference<Map<String, String>>() {
            });
        } else {
            dbMap = new HashMap<>(0);
        }
        String jdbcUrl = String.format("jdbc:mysql://%s:%s/socketTimeout=%s", host, port, 36000);
        MySqlSourceBuilder<String> mysqlSourceBuilder = new MySqlSourceBuilder() //
                .hostname(host).port(port).username(username).password(password) //
                .deserializer(deserializer) //
                .debeziumProperties(dzProperties) //
                .splitSize(parameterTool.getInt("mysql.splitSize", 100000)) // the split size of each snapshot split
                .distributionFactorUpper(10);// for not discontinuous primary keys
        if (databaseList != null && databaseList.length() > 0) {
            mysqlSourceBuilder.databaseList(databaseList.split(","));
        }
        if (tableList != null && tableList.length() > 0) {
            mysqlSourceBuilder.tableList(tableList.split(","));
        }
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
        // stream data
        long start = System.currentTimeMillis();
        env.fromSource(mysqlSourceBuilder.build(), WatermarkStrategy.noWatermarks(), "mysql_source").setParallelism(1)//
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
                        Class.forName("org.postgresql.Driver");
                        Class.forName("com.mysql.jdbc.Driver");
                        try (Connection pgConn = DriverManager.getConnection(jdbcUrl, username, password); //
                             Connection srConn = DriverManager.getConnection(srJdbcUrl, srUsername, srPassword)) {
                            compareSchema(pgConn, schema, table, srConn, targetSchema);
                        }
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
                }).setParallelism(2).keyBy(row -> row.getDatabase() + "." + row.getTable()) // key by table
                .addSink(SinkFunctionFactory.createSinkFunction(srOptions)).setParallelism(4).name("sr_sink");
        // .addSink(new DiscardingSink()).setParallelism(4).name("sr_sink");
        // start
        env.execute(jobName);
        log.info("{} started", MysqlToStarRocksJob.class.getSimpleName());
    }

    private synchronized static void compareSchema(Connection pgConn, String srcSchema, String srTable, Connection srConn, String targetSchema) throws SQLException {
        log.info("compareSchema start");
        long start = System.currentTimeMillis();
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
            waitSrAltering(srConn, targetSchema);
        }
        log.info("compareSchema end, time={}", System.currentTimeMillis() - start);
    }

    private static void waitSrAltering(Connection srConn, String schema) throws SQLException {
        String sql = String.format("show alter table column from %s order by createtime desc limit 100", schema);
        while (true) {
            log.info("waitSrAltering running");
            boolean isBreak = true;
            try (Statement st = srConn.createStatement()) {
                try (ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        if ("!FINISHED".equals(rs.getString("State"))) {
                            isBreak = false;
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
