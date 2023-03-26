package com.demo.ck;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.dynamic.datasource.tx.ConnectionFactory;
import com.baomidou.dynamic.datasource.tx.TransactionContext;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class DataJdbcImportTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    ExecutorService executorService = new ThreadPoolExecutor(6, 6, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    private AtomicInteger count = new AtomicInteger();
    private long start = System.currentTimeMillis();

    @Test
    void testInsert5() throws Exception {
        testInsert("");
        testInsert("1");
        testInsert("2");
        testInsert("3");
    }

    @Test
    void testInsert() throws Exception {
        testInsert("");
    }

    void testInsert(String i) throws Exception {
        // 导入2000w数据
        String sql = "INSERT INTO w2000p (c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32,c33 ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream("/f/data/2000w"), "gbk"));
        List<Object[]> argList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] ss = line.split(",");
            if (ss.length != 33) {
                continue;
            }
            ss[4] = ss[4] + i;
            argList.add(ss);
            if (argList.size() >= 5000) {
                this.batchUpdate(sql, argList, start);
                argList = new ArrayList<>();
            }
        }
        if (argList.size() > 0) {
            this.batchUpdate(sql, argList, start);
        }
    }

    private void batchUpdate(String sql, List<Object[]> argList, long start) {
        executorService.execute(() -> {
            DynamicDataSourceContextHolder.push("mysql");
            TransactionContext.bind(UUID.randomUUID().toString());
            jdbcTemplate.batchUpdate(sql, argList);
            ConnectionFactory.notify(true);
            TransactionContext.remove();
            count.addAndGet(argList.size());
            long cost = System.currentTimeMillis() - start;
            System.out.println("cost count=" + count + ",cost=" + cost);
        });
    }
}
