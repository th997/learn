package com.demo.flink;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.base.CaseFormat;
import org.apache.flink.shaded.guava30.com.google.common.collect.ImmutableMap;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PostgresToElasticSearchJob {
    final static Logger log = org.slf4j.LoggerFactory.getLogger(PostgresToElasticSearchJob.class);
    final static String SQL_SOURCE = "create temporary table %s_source (%s) with (%s)";
    final static String SQL_SINK = "create temporary table %s_sink (%s) with (%s)";

    public static void main(String[] args) throws Exception {
        log.info("{} start...", PostgresToElasticSearchJob.class.getSimpleName());
        // read config
        String jobEnv = ParameterTool.fromArgs(args).get("env", "local");
        String configName = String.format("config-%s.properties", jobEnv);
        InputStream in = PostgresToElasticSearchJob.class.getClassLoader().getResourceAsStream(configName);
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(in);
        // create the execution environment
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        TableEnvironment env = TableEnvironment.create(settings);
        String jobName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, PostgresToElasticSearchJob.class.getSimpleName());
        env.getConfig().addConfiguration( //
                Configuration.fromMap(ImmutableMap.of(//
                        "metrics.reporter.jmx.factory.class", "org.apache.flink.metrics.jmx.JMXReporterFactory",//
                        "metrics.reporter.jmx.port", "9020",//
                        "metrics.enabled", "false")));
        for (String table : parameterTool.get("pg.tableList").split(",")) {
            table = table.split("\\.")[1];
            // table source
            Map<String, Object> pgConfig = new HashMap<>();
            pgConfig.put("connector", "postgres-cdc");
            pgConfig.put("hostname", parameterTool.get("pg.host"));
            pgConfig.put("port", parameterTool.get("pg.port"));
            pgConfig.put("username", parameterTool.get("pg.username"));
            pgConfig.put("password", parameterTool.get("pg.password"));
            pgConfig.put("database-name", parameterTool.get("pg.database"));
            pgConfig.put("schema-name", parameterTool.get("pg.schemaList"));
            pgConfig.put("debezium.plugin.name", "pgoutput");
            pgConfig.put("changelog-mode", "upsert");
            pgConfig.put("chunk-key.even-distribution.factor.upper-bound", 20);
            pgConfig.put("table-name", table);
            pgConfig.put("slot.name", jobName + "_" + table);
            StringBuffer pgConfigStr = new StringBuffer();
            pgConfig.forEach((k, v) -> {
                pgConfigStr.append(String.format("'%s'='%s',\n", k, v));
            });
            pgConfigStr.delete(pgConfigStr.length() - 2, pgConfigStr.length());
            String pgSql = String.format(SQL_SOURCE, table, getTableColumns(table), pgConfigStr);
            env.executeSql(pgSql);
        }
        // table sink
        String index = "user_info";
        Map<String, Object> esConfig = new HashMap<>();
        esConfig.put("connector", "elasticsearch-7");
        esConfig.put("hosts", parameterTool.get("es.hosts"));
        esConfig.put("username", parameterTool.get("es.username"));
        esConfig.put("password", parameterTool.get("es.password"));
        esConfig.put("index", index);
        esConfig.put("format", "json");
        StringBuffer esConfigStr = new StringBuffer();
        esConfig.forEach((k, v) -> {
            esConfigStr.append(String.format("'%s'='%s',\n", k, v));
        });
        esConfigStr.delete(esConfigStr.length() - 2, esConfigStr.length());
        String esSql = String.format(SQL_SINK, index, "id int, user_code string, user_name string, user_info string, update_time timestamp ,primary key (id) not enforced", esConfigStr);
        env.executeSql(esSql);
        // table stream
        env.executeSql("insert into user_info_sink(id,user_code,user_name,user_info,update_time)" + "select t1.id,t1.user_code,t1.user_name,t2.user_info,t1.update_time from user_source t1 " + "left join user_detail_source t2 on t1.user_code=t2.user_code");
        // start
        log.info("{} started", PostgresToElasticSearchJob.class.getSimpleName());
    }

    private static String getTableColumns(String tableName) {
        switch (tableName) {
            case "user":
                return "id int, user_code string, user_name string, update_time timestamp, primary key (id) not enforced";
            case "user_detail":
                return "id int, user_code string, user_info string, update_time timestamp, primary key (id) not enforced";
            default:
                throw new IllegalArgumentException("Unknown table: " + tableName);
        }
    }
}
