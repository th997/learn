package com.demo.flink;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.starrocks.connector.flink.table.data.DefaultStarRocksRowData;
import com.starrocks.connector.flink.table.sink.SinkFunctionFactory;
import com.starrocks.connector.flink.table.sink.StarRocksSinkOptions;
import com.ververica.cdc.connectors.postgres.source.PostgresSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.shaded.guava30.com.google.common.base.CaseFormat;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.connect.json.DecimalFormat;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PostgresToStarRocksJob {
    static Logger log = org.slf4j.LoggerFactory.getLogger(PostgresToStarRocksJob.class);

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
            env.getCheckpointConfig().setCheckpointStorage(Path.fromLocalFile(new File("/d/tmp")));
        } else {
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        env.enableCheckpointing(3000, CheckpointingMode.AT_LEAST_ONCE);
        String jobName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, PostgresToStarRocksJob.class.getSimpleName());
        // json serialization
        Map<String, Object> customConverterConfigs = new HashMap<>();
        customConverterConfigs.put(JsonConverterConfig.DECIMAL_FORMAT_CONFIG, DecimalFormat.NUMERIC.name());
        JsonDebeziumDeserializationSchema deserializer = new JsonDebeziumDeserializationSchema(false, customConverterConfigs);
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        om.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        om.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        // pg source
        PostgresSourceBuilder.PostgresIncrementalSource<String> pgSource = PostgresSourceBuilder.PostgresIncrementalSource.<String>builder() //
                .hostname(parameterTool.get("pg.host")) //
                .port(parameterTool.getInt("pg.port"))//
                .username(parameterTool.get("pg.username"))//
                .password(parameterTool.get("pg.password")) //
                .database(parameterTool.get("pg.database"))//
                .schemaList(parameterTool.get("pg.schemaList").split(","))//
                .tableList(parameterTool.get("pg.tableList").split(","))//
                .slotName(jobName) //
                .decodingPluginName("pgoutput") // pg default
                .deserializer(deserializer) //
                .splitSize(20000) // the split size of each snapshot split
                .distributionFactorUpper(10) // for not discontinuous primary keys
                .includeSchemaChanges(true) //
                .build();
        // starrocks sink
        StarRocksSinkOptions srOptions = StarRocksSinkOptions.builder() //
                .withProperty("jdbc-url", parameterTool.get("sr.jdbc-url"))//
                .withProperty("load-url", parameterTool.get("sr.load-url"))//
                .withProperty("database-name", parameterTool.get("sr.database-name"))//
                .withProperty("table-name", "xxx") // not use , but not empty
                .withProperty("username", parameterTool.get("sr.username"))//
                .withProperty("password", parameterTool.get("sr.password"))//
                .withProperty("sink.properties.format", "json")//
                .withProperty("sink.properties.strict_mode", "true")//
                .build();
        srOptions.enableUpsertDelete();
        // stream data
        env.fromSource(pgSource, WatermarkStrategy.noWatermarks(), "pg_source").setParallelism(1)//
                .map(data -> {
                    // https://debezium.io/documentation/reference/stable/connectors/postgresql.html#postgresql-events
                    JsonNode json = om.readTree(data);
                    String op = json.get("op").asText();
                    JsonNode source = json.get("source");
                    JsonNode before = json.get("before");
                    JsonNode after = json.get("after");
                    DefaultStarRocksRowData row = new DefaultStarRocksRowData();
                    row.setDatabase(srOptions.getDatabaseName());
                    row.setTable(source.get("table").asText());
                    if ("d".equals(op)) {
                        ((ObjectNode) before).put("__op", 1);// delete tag
                        row.setRow(before.toString());
                    } else {
                        row.setRow(after.toString());
                    }
                    return row;
                }).keyBy(row -> row.getDatabase() + "." + row.getTable()) // key by table
                .addSink(SinkFunctionFactory.createSinkFunction(srOptions)).name("sr_sink");
        // start
        env.execute(jobName);
        log.info("{} started", PostgresToStarRocksJob.class.getSimpleName());
    }
}
