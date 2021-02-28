package com.github.th997.demo.hbase;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;

@Service
public class HbaseService {

    private final static Logger LOG = LoggerFactory.getLogger(HbaseService.class);
    @Autowired
    private Connection connection;

    public boolean createTable(String tableName, List<String> columnFamily, byte[][] splitKeys) {
        try (Admin admin = connection.getAdmin()) {
            List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>(columnFamily.size());
            columnFamily.forEach(
                    cf -> familyDescriptors.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build()));
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(familyDescriptors).build();

            if (admin.tableExists(TableName.valueOf(tableName))) {
                LOG.info("table Exists!");
            } else {
                if (splitKeys != null && splitKeys.length > 0) {
                    admin.createTable(tableDescriptor, splitKeys);
                } else {
                    admin.createTable(tableDescriptor);
                }
                LOG.info("create table Success !");
            }
        } catch (IOException e) {
            LOG.error("create table error,tableName={}", tableName, e);
            return false;
        }
        return true;
    }

    public byte[][] getSplitKeys(String[] keys) {
        if (keys == null) {
            keys = new String[]{"10|", "20|", "30|", "40|", "50|", "60|", "70|", "80|", "90|"};
        }
        byte[][] splitKeys = new byte[keys.length][];
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < keys.length; i++) {
            rows.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> rowKeyIter = rows.iterator();
        int i = 0;
        while (rowKeyIter.hasNext()) {
            byte[] tempRow = rowKeyIter.next();
            rowKeyIter.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        return splitKeys;
    }

    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        try (Admin admin = connection.getAdmin()) {
            TableName[] tableNames = admin.listTableNames();
            for (TableName tableName : tableNames) {
                result.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            LOG.error("get tables error", e);
        }
        return result;
    }

    /**
     * 按startKey和endKey，分区数获取分区
     */
    public byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%016x", key).getBytes();
            splits[i] = b;
        }
        return splits;
    }

    /**
     * 获取table
     *
     * @param tableName 表名
     * @return Table
     * @throws IOException IOException
     */
    private Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * 遍历查询指定表中的所有数据
     *
     * @param tableName 表名
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */

    public Map<String, Map<String, String>> getResultScanner(String tableName) {
        Scan scan = new Scan();
        return this.queryData(tableName, scan);
    }

    /**
     * 遍历查询指定表中的所有数据
     *
     * @param tableName 表名
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */

    public Map<String, Map<String, String>> getResultTimeScanner(String tableName, long minStamp, long maxStamp) throws IOException {
        Scan scan = new Scan();
        scan.setTimeRange(minStamp, maxStamp);
        return this.queryData(tableName, scan);
    }

    /**
     * 根据startRowKey和stopRowKey遍历查询指定表中的所有数据
     *
     * @param tableName   表名
     * @param startRowKey 起始rowKey
     * @param stopRowKey  结束rowKey
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> getResultScanner(String tableName, String startRowKey, String stopRowKey) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(startRowKey) && StringUtils.isNotBlank(stopRowKey)) {
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(stopRowKey));
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 通过行前缀过滤器查询数据
     *
     * @param tableName 表名
     * @param prefix    以prefix开始的行键
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> getResultScannerPrefixFilter(String tableName, String prefix) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(prefix)) {
            Filter filter = new PrefixFilter(Bytes.toBytes(prefix));
            scan.setFilter(filter);
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 通过列前缀过滤器查询数据
     *
     * @param tableName 表名
     * @param prefix    以prefix开始的列名
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> getResultScannerColumnPrefixFilter(String tableName, String prefix) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(prefix)) {
            Filter filter = new ColumnPrefixFilter(Bytes.toBytes(prefix));
            scan.setFilter(filter);
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 查询行键中包含特定字符的数据
     *
     * @param tableName 表名
     * @param keyword   包含指定关键词的行键
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> getResultScannerRowFilter(String tableName, String keyword) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(keyword)) {
            Filter filter = new RowFilter(CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
            scan.setFilter(filter);
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 查询列名中包含特定字符的数据
     *
     * @param tableName 表名
     * @param keyword   包含指定关键词的列名
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> getResultScannerQualifierFilter(String tableName, String keyword) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(keyword)) {
            Filter filter = new QualifierFilter(CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
            scan.setFilter(filter);
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 通过表名以及过滤条件查询数据
     *
     * @param tableName 表名
     * @param scan      过滤条件
     * @return java.util.Map<java.lang.String, java.util.Map < java.lang.String,
            * java.lang.String>>
     */
    public Map<String, Map<String, String>> queryData(String tableName, Scan scan) {
        // <rowKey,对应的行数据>
        Map<String, Map<String, String>> result = new HashMap<>();
        // 获取表
        try (Table table = getTable(tableName)) {
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
                        result.put(rowKey, columnMap);
                    }
                }
            } catch (IOException e) {
                LOG.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}", tableName), e);
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}", tableName), e);
        }
        return result;
    }

    /**
     * 根据tableName和rowKey精确查询一行的数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @return java.util.Map<java.lang.String, java.lang.String> 返回一行的数据
     */
    public Map<String, String> getRowData(String tableName, String rowKey) {
        // 返回的键值对
        Map<String, String> result = new HashMap<>();
        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        try (Table table = getTable(tableName)) {

            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {
                    result.put(
                            Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                                    cell.getQualifierLength()),
                            Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("查询一行的数据失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
        }
        return result;
    }

    /**
     * 根据tableName、rowKey、familyName、column查询指定单元格的数据
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列族名
     * @param columnName 列名
     * @return java.lang.String
     */
    public String getColumnValue(String tableName, String rowKey, String familyName, String columnName) {
        String str = null;
        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        try (Table table = getTable(tableName)) {
            Result result = table.get(get);
            if (result != null && !result.isEmpty()) {
                Cell cell = result.getColumnLatestCell(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
                if (cell != null) {
                    str = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("查询指定单元格的数据失败,tableName:{0},rowKey:{1},familyName:{2},columnName:{3}",
                    tableName, rowKey, familyName, columnName), e);
        }
        return str;
    }

    /**
     * 根据tableName、rowKey、familyName、column查询指定单元格多个版本的数据
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列族名
     * @param columnName 列名
     * @param versions   需要查询的版本数
     * @return java.util.List<java.lang.String>
     */
    public List<String> getColumnValuesByVersion(String tableName, String rowKey, String familyName, String columnName,
                                                 int versions) {
        // 返回数据
        List<String> result = new ArrayList<>(versions);
        // 获取表
        try (Table table = getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
            // 读取多少个版本
            get.readVersions(versions);
            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {
                    result.add(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("查询指定单元格多个版本的数据失败,tableName:{0},rowKey:{1},familyName:{2},columnName:{3}",
                    tableName, rowKey, familyName, columnName), e);
        }
        return result;
    }

    /**
     * 为表添加 or 更新数据
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列族名
     * @param columns    列名数组
     * @param values     列值得数组
     */

    public void putData(String tableName, String rowKey, String familyName, String[] columns, String[] values) {
        // 获取表
        try (Table table = getTable(tableName)) {
            putData(table, rowKey, tableName, familyName, columns, values);
        } catch (Exception e) {
            LOG.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
                    familyName), e);
        }
    }

    /**
     * 为表添加 or 更新数据
     *
     * @param table      Table
     * @param rowKey     rowKey
     * @param tableName  表名
     * @param familyName 列族名
     * @param columns    列名数组
     * @param values     列值得数组
     */
    private void putData(Table table, String rowKey, String tableName, String familyName, String[] columns,
                         String[] values) {
        try {
            // 设置rowkey
            Put put = new Put(Bytes.toBytes(rowKey));
            if (columns != null && values != null && columns.length == values.length) {
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i] != null && values[i] != null) {
                        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
                    } else {
                        throw new NullPointerException(
                                MessageFormat.format("列名和列数据都不能为空,column:{0},value:{1}", columns[i], values[i]));
                    }
                }
            }
            table.put(put);
            LOG.debug("putData add or update data Success,rowKey:" + rowKey);
//            table.close();
        } catch (Exception e) {
            LOG.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}", tableName, rowKey,
                    familyName), e);
        }
    }

    /**
     * 为表的某个单元格赋值
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列族名
     * @param column1    列名
     * @param value1     列值
     */

    public void setColumnValue(String tableName, String rowKey, String familyName, String column1, String value1) {
        // 获取表
        try (Table table = getTable(tableName)) {
            // 设置rowKey
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column1), Bytes.toBytes(value1));
            table.put(put);
            LOG.debug("add data Success!");
        } catch (IOException e) {
            LOG.error(MessageFormat.format("为表的某个单元格赋值失败,tableName:{0},rowKey:{1},familyName:{2},column:{3}", tableName,
                    rowKey, familyName, column1), e);
        }
    }

    /**
     * 删除指定的单元格
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列族名
     * @param columnName 列名
     * @return boolean
     */

    public boolean deleteColumn(String tableName, String rowKey, String familyName, String columnName) {
        try (Admin admin = connection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                // 获取表
                try (Table table = getTable(tableName)) {
                    Delete delete = new Delete(Bytes.toBytes(rowKey));
                    // 设置待删除的列
                    delete.addColumns(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
                    table.delete(delete);
                    LOG.debug(
                            MessageFormat.format("familyName({0}):columnName({1})is deleted!", familyName, columnName));
                }
            }

        } catch (IOException e) {
            LOG.error(MessageFormat.format("删除指定的列失败,tableName:{0},rowKey:{1},familyName:{2},column:{3}", tableName,
                    rowKey, familyName, columnName), e);
            return false;
        }
        return true;
    }

    /**
     * 根据rowKey删除指定的行
     *
     * @param tableName 表名
     * @param rowKey    rowKey
     * @return boolean
     */
    public boolean deleteRow(String tableName, String rowKey) {
        try (Admin admin = connection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                // 获取表
                try (Table table = getTable(tableName)) {
                    Delete delete = new Delete(Bytes.toBytes(rowKey));
                    table.delete(delete);
                    LOG.debug(MessageFormat.format("row({0}) is deleted!", rowKey));
                }
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("删除指定的行失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
            return false;
        }
        return true;
    }

    /**
     * 根据columnFamily删除指定的列族
     *
     * @param tableName    表名
     * @param columnFamily 列族
     * @return boolean
     */

    public boolean deleteColumnFamily(String tableName, String columnFamily) {
        try (Admin admin = connection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                admin.deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(columnFamily));
                LOG.debug(MessageFormat.format("familyName({0}) is deleted!", columnFamily));
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("删除指定的列族失败,tableName:{0},columnFamily:{1}", tableName, columnFamily), e);
            return false;
        }
        return true;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public boolean deleteTable(String tableName) {
        try (Admin admin = connection.getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
                admin.deleteTable(TableName.valueOf(tableName));
                LOG.debug(tableName + "is deleted!");
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("删除指定的表失败,tableName:{0}", tableName), e);
            return false;
        }
        return true;
    }
}