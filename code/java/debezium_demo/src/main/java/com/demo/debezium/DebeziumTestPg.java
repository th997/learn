package com.demo.debezium;

import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class DebeziumTestPg {
    public static void main(String[] args) {
        Configuration config = Configuration.create()
                .with("name", "debezium-connector")
                .with("topic.prefix", "debezium-topic")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                // 指定offset存储
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/pg_offsets.dat")
                .with("offset.flush.interval.ms", 60000)
                // 
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/pg_history.dat")
                .with("database.history.refer.ddl", true)
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", "/tmp/pg_schemahistory.dat")
                // 数据库配置
                .with("database.hostname", "10.10.10.106")
                .with("database.port", 55432)
                .with("database.user", "postgres")
                .with("database.password", "postgres_666888")
                .with("slot.name", "debezium1")
                .with("plugin.name", "pgoutput")
                .with("database.connectionTimeZone", "Asia/Shanghai")
                // 指定库表
                .with("database.dbname", "postgres")
                .with("scheme.include.list", "datatest")
                .with("table.include.list", "datatest.w2000")
                .with("include.schema.changes", "true")
                .with("snapshot.mode", "initial")
                .build();

        AtomicLong count = new AtomicLong();
        long start = System.currentTimeMillis();
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(record -> {
                    long co = count.incrementAndGet();
                    if (co % 10000 == 0) {
                        System.out.println("count=" + co + ",time=" + (System.currentTimeMillis() - start));
                    }
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
