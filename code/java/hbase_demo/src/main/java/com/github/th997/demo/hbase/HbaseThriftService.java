//package com.github.th997.demo.hbase;
//
//import java.nio.ByteBuffer;
//import java.text.MessageFormat;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.hadoop.hbase.thrift2.generated.TColumnValue;
//import org.apache.hadoop.hbase.thrift2.generated.TGet;
//import org.apache.hadoop.hbase.thrift2.generated.THBaseService;
//import org.apache.hadoop.hbase.thrift2.generated.TPut;
//import org.apache.hadoop.hbase.thrift2.generated.TResult;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TTransport;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class HbaseThriftService {
//
//	private final static Logger LOG = LoggerFactory.getLogger(HbaseThriftService.class);
//	@Autowired
//	private HBaseConfig haBaseConfig;
//
//	// https://blog.csdn.net/chinabestchina/article/details/83217872
//	// https://github.com/apache/hbase/blob/master/hbase-examples/src/main/java/org/apache/hadoop/hbase/thrift2/DemoClient.java
//	public void setColumnValue(String tableName, String rowKey, String familyName, String column1, String value1) {
//		try (TTransport transport = haBaseConfig.transport()) {
//			// 协议
//			TProtocol protocol = new TBinaryProtocol(transport);
//			THBaseService.Iface client = new THBaseService.Client(protocol);
//			transport.open();
//			// 操作
//			TPut put = new TPut();
//			put.setRow(rowKey.getBytes());
//			put.setColumnValues(Arrays.asList(new TColumnValue(ByteBuffer.wrap(familyName.getBytes()),
//					ByteBuffer.wrap(column1.getBytes()), ByteBuffer.wrap(value1.getBytes()))));
//			client.put(ByteBuffer.wrap(tableName.getBytes()), put);
//
//		} catch (Exception e) {
//			LOG.error(MessageFormat.format("写入失败,tableName:{0}", tableName), e);
//		}
//	}
//
//	public Map<String, String> getRowData(String tableName, String rowKey) {
//		Map<String, String> result = new HashMap<>();
//		try (TTransport transport = haBaseConfig.transport()) {
//			// 协议
//			TProtocol protocol = new TBinaryProtocol(transport);
//			THBaseService.Iface client = new THBaseService.Client(protocol);
//			transport.open();
//			// 查询
//			TGet tget = new TGet(ByteBuffer.wrap(rowKey.getBytes()));
//			// tget.setColumns(Arrays.asList(new TColumn()));
//			TResult tResult = client.get(ByteBuffer.wrap(tableName.getBytes()), tget);
//			for (TColumnValue tValue : tResult.getColumnValues()) {
//				result.put(new String(tValue.getQualifier()), new String(tValue.getValue()));
//			}
//		} catch (Exception e) {
//			LOG.error(MessageFormat.format("查询一行的数据失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
//		}
//		return result;
//	}
//}