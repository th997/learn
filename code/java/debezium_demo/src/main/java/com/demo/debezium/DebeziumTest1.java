package com.demo.debezium;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebeziumTest1 {
    public static void main(String[] args) {
        Configuration config = Configuration.create()
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/offsets.dat")
                .with("offset.flush.interval.ms", 60000)
                .with("name", "mysql-connector")
                .with("database.hostname", "10.10.10.106")
                .with("database.port", 53307)
                .with("database.user", "root")
                .with("database.password", "mysql_666888")
                .with("database.server.id", "184054")
                .with("database.server.name", "mysql-db")
                .with("database.include.list", "test")
                .with("database.connectionTimeZone", "Asia/Shanghai")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/dbhistory.dat")
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", "/tmp/schemahistory.dat")
                .with("include.schema.changes", "true")
                .with("snapshot.mode", "initial")
                .with("topic.prefix", "my-app-connector")
                .build();

        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(record -> {
                    System.out.println(record.value()); // 处理变更事件
                }).build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                engine.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            executor.shutdown();
        }));
    }
}
