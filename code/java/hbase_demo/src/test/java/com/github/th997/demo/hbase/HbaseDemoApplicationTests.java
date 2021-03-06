package com.github.th997.demo.hbase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.hbase.client.Scan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseDemoApplicationTests {

    @Test
    public void contextLoads() {
    }

//	docker run -d -h myhbase \
//	-p 2181:2181 \
//	-p 8080:8080 \
//	-p 8085:8085 \
//	-p 9090:9090 \
//	-p 9095:9095 \
//	-p 16000:16000 \
//	-p 16010:16010 \
//	-p 16201:16201 \
//	-p 16301:16301 \
//	-p 16020:16020 \
//	--name hbase harisekhon/hbase

    @Autowired
    HBaseConfig config;
    @Autowired
    org.apache.hadoop.conf.Configuration configuration;
    @Autowired
    HbaseService hbaseService;
    @Autowired
    HbaseThriftService hbaseThriftService;

    @Test
    public void testConfig() {
        System.out.println(config.getProp());
        System.out.println(configuration);
    }

    @Test
    public void testTable() {
        hbaseService.createTable("user", Arrays.asList("a"), null);
        hbaseService.createTable("user1", Arrays.asList("a"), hbaseService.getSplitKeys(null));
        List<String> tables = hbaseService.getAllTableNames();
        System.out.println(tables);
    }

    @Test
    public void testWrite() {
        ExecutorService es = Executors.newFixedThreadPool(100);
        long start = System.currentTimeMillis();
        AtomicInteger ac = new AtomicInteger();
        int count = 1000 * 10000;
        for (int i = 0; i < count; i++) {
            final int loc = i;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    String column = "a" + (char) (loc % 'z');
                    hbaseService.setColumnValue("user", loc + "", "a", column, Math.random() + "");
                    int co = ac.addAndGet(1);
                    if (co % 10000 == 0) {
                        System.out.println("time:" + (System.currentTimeMillis() - start) + ",num=" + co + ",time=" + System.currentTimeMillis());
                    }
                }
            });
            // break;

        }
        while (ac.get() < count - 1) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

    }

    @Test
    public void testRead() {
        ExecutorService es = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        AtomicInteger ac = new AtomicInteger();
        for (int i = 0; i < 1000000; i++) {
            final int loc = i;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    Object obj = hbaseService.getRowData("user", loc + "");
                    // System.out.println(obj);
                    int co = ac.addAndGet(1);
                    if (co % 10000 == 0) {
                        System.out.println("time:" + (System.currentTimeMillis() - start) + ",num=" + co);
                    }
                }
            });
            // break;

        }

        while (ac.get() < 1000000 - 1) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

    }

    @Test
    public void testReadTime() throws IOException {
        Scan scan = new Scan();
        scan.setTimeRange(1614520814424L, 1614520814425L);
        //scan.setTimeRange(1614521358297L, 1614521358298L);

        scan.setCacheBlocks(false);
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> result = hbaseService.queryData("user", scan);
        result = new TreeMap<>(result);
        for (String key : result.keySet()) {
            System.out.println(key + "=" + result.get(key));
        }
        System.out.println("time=" + (System.currentTimeMillis() - start));
    }

}
