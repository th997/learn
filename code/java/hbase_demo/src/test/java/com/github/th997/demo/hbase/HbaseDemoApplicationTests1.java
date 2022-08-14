package com.github.th997.demo.hbase;

import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseDemoApplicationTests1 {

    @Autowired
    HbaseService hbaseService;

    String tableName = "test2";
    String family = "cf";

    @Test
    public void testFlush() throws Exception {
        try (Admin admin = hbaseService.getAdmin()) {
            admin.flush(TableName.valueOf("user1"));
        }
    }

    @Test
    public void testCreateTable() throws Exception {
        try (Admin admin = hbaseService.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            if (admin.tableExists(table)) {
                admin.disableTable(table);
                admin.deleteTable(table);
            }
            ColumnFamilyDescriptor cf = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family)).build();
            TableDescriptor td = TableDescriptorBuilder.newBuilder(table).setColumnFamily(cf).build();
            int numRegions = 4;
            int start = 0xff / numRegions;
            int end = 0xff - start;
            admin.createTable(td, new byte[]{(byte) start}, new byte[]{(byte) end}, numRegions);
        }
    }

    @Test
    public void testSplit() throws Exception {
        try (Admin admin = hbaseService.getAdmin()) {
            TableName table = TableName.valueOf(tableName);
            List<RegionInfo> regionList = admin.getRegions(table);
            for (RegionInfo region : regionList) {
                System.out.println(region);
            }
            admin.split(table);
        }
    }

    @Test
    public void testTable() {
        List<String> tables = hbaseService.getAllTableNames();
        System.out.println(tables);
    }

    @Test
    public void testWrite() throws IOException {
        String[] columns = new String[]{"c1", "c2", "c3", "c4"};
        Table table = hbaseService.getTable(tableName);
        List<Put> list = new ArrayList<>();
        for (int i = 30000000; i < 100000000; i++) {
            Put put = new Put(DigestUtils.md5Digest(Bytes.toBytes(i)));
            for (String col : columns) {
                put.addColumn(family.getBytes(), col.getBytes(), Bytes.toBytes(col + i));
            }
            list.add(put);
            if (list.size() >= 1000) {
                table.put(list);
                list.clear();
            }
//            String[] values = new String[]{"v1" + i, "v2" + i, "v3" + i};
            //   hbaseService.putData(tableName, rowKey, family, columns, values);
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
