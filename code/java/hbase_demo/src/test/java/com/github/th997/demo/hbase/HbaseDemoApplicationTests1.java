package com.github.th997.demo.hbase;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseDemoApplicationTests1 {

    @Autowired
    HbaseService hbaseService;

    String tableName = "test1";
    String family = "a";

    @Test
    public void testTable() {
        hbaseService.createTable(tableName, Arrays.asList(family), null);
        List<String> tables = hbaseService.getAllTableNames();
        System.out.println(tables);
    }

    @Test
    public void testWrite() {
        String[] columns = new String[]{"c1", "c2", "c3"};
        for (int i = 0; i < 10; i++) {
            String rowKey = "k" + i;
            String[] values = new String[]{"v1" + i, "v2" + i, "v3" + i};
            hbaseService.putData(tableName, rowKey, family, columns, values);
        }
    }

    @Test
    public void testRead1() throws IOException {
        Scan scan = new Scan();
        Map<String, Map<String, String>> result = hbaseService.queryData(tableName, scan);
        result = new TreeMap<>(result);
        for (String key : result.keySet()) {
            System.out.println(key + "=" + result.get(key));
        }
    }

    @Test
    public void testReadTime() throws IOException {
        Scan scan = new Scan();
        scan.setTimeRange(System.currentTimeMillis() - 200 * 1000 * 60, System.currentTimeMillis());
        Map<String, Map<String, String>> result = hbaseService.queryData(tableName, scan);
        result = new TreeMap<>(result);
        for (String key : result.keySet()) {
            System.out.println(key + "=" + result.get(key));
        }
    }

}
