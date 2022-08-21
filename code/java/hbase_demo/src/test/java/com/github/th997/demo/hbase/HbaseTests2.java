package com.github.th997.demo.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseTests2 {

    @Autowired
    private Connection connection;

    @Test
    public void testInitTable() throws IOException {
        String tableName = "test1";
        String familyName = "cf";
        TableName table = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            if (admin.tableExists(table)) {
                admin.disableTable(table);
                admin.deleteTable(table);
            }
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName))
                    //  .setCompressionType(Compression.Algorithm.LZ4)
                    .build();
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(Arrays.asList(columnFamilyDescriptor))
                    .build();
            admin.createTable(tableDescriptor);
        }
    }

    @Test
    public void testBatchWrite() throws Exception {
        String tableName = "user";
        String familyName = "cf";
        int colLen = 10;
        int rowStart = 1000000;
        int rowLen = 4000000;
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            long start = System.currentTimeMillis();
            List<Put> puts = new ArrayList<>();
            for (int i = rowStart; i <= rowStart + rowLen; i++) {
                Put put = new Put(Bytes.toBytes("rowKeyValue" + i));
                for (int j = 0; j < colLen; j++) {
                    put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes("columnName" + j), Bytes.toBytes("v" + j + "," + i));
                }
                puts.add(put);
                if (i % 10000 == 0) {
                    table.put(puts);
                    puts.clear();
                    System.out.println("time:" + (System.currentTimeMillis() - start) + ",num=" + i + ",now=" + System.currentTimeMillis());
                }
            }
        }
    }

    @Test
    public void testGet() throws Exception {
        String tableName = "user";
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes("rowKeyValue" + 123));
            table.get(get);
            // get
            long start = System.currentTimeMillis();
            Map<String, String> data = new HashMap<>();
            get = new Get(Bytes.toBytes("rowKeyValue" + 453));
            Result result = table.get(get);
            for (Cell cell : result.listCells()) {
                data.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            }
            System.out.println("time:" + (System.currentTimeMillis() - start) + ",data=" + data);
        }
    }

    @Test
    public void testScan() throws Exception {
        String tableName = "user";
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes("rowKeyValue" + 123));
            table.get(get);
            long start = System.currentTimeMillis();
            Map<String, Object> data = new HashMap<>();
            Scan scan = new Scan();
            byte[] prefix = Bytes.toBytes("rowKeyValue123467");

//            // quickly
//            scan.setRowPrefixFilter(prefix);
//
//            // slowly ,full table scan likely
//            scan.setFilter(new PrefixFilter(prefix));
//
//            // slowly ,full table scan
//            scan.setFilter(new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(prefix)));
//
//            // quickly
//            scan.setFilter(this.getMultiRowRangeFilter(prefix));
//
//            // only query col
//            scan.setRowPrefixFilter(prefix);
//            scan.setFilter(new ColumnValueFilter(Bytes.toBytes("cf"), Bytes.toBytes("columnName7"), CompareOperator.EQUAL, Bytes.toBytes("v7,1234677")));
//
//            // quickly
//            scan.withStartRow(prefix);
//            scan.setFilter(new PrefixFilter(prefix));

            // quickly
            scan.setRowPrefixFilter(prefix);
            scan.setFilter(new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(prefix)));


            try (ResultScanner rs = table.getScanner(scan)) {
                for (Result r : rs) {
                    // 每一行数据
                    Map<String, String> columnMap = new HashMap<>();
                    String rowKey = null;
                    for (Cell cell : r.listCells()) {
                        if (rowKey == null) {
                            rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        }
                        columnMap.put(
                                Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                                        cell.getQualifierLength()),
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                    if (rowKey != null) {
                        data.put(rowKey, columnMap);
                    }
                }
            }
            System.out.println("time:" + (System.currentTimeMillis() - start) + ",data=" + data);
        }
    }

    private MultiRowRangeFilter getMultiRowRangeFilter(byte[]... prefixes) {
        MultiRowRangeFilter filter = new MultiRowRangeFilter(prefixes);
        return filter;
    }
}
