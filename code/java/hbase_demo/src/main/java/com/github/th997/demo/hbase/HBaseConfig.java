package com.github.th997.demo.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "hbase")
public class HBaseConfig {

	private static Logger LOG = LoggerFactory.getLogger(HBaseConfig.class);

	private static Connection connection;

	private Map<String, String> prop;

	private String thriftHost;

	private int thriftPort;

	private int thriftSocketTimeout = 3000;

	private int thriftConnectTimeout = 3000;

	public int getThriftSocketTimeout() {
		return thriftSocketTimeout;
	}

	public void setThriftSocketTimeout(int thriftSocketTimeout) {
		this.thriftSocketTimeout = thriftSocketTimeout;
	}

	public int getThriftConnectTimeout() {
		return thriftConnectTimeout;
	}

	public void setThriftConnectTimeout(int thriftConnectTimeout) {
		this.thriftConnectTimeout = thriftConnectTimeout;
	}

	public String getThriftHost() {
		return thriftHost;
	}

	public void setThriftHost(String thriftHost) {
		this.thriftHost = thriftHost;
	}

	public int getThriftPort() {
		return thriftPort;
	}

	public void setThriftPort(int thriftPort) {
		this.thriftPort = thriftPort;
	}

	public static Connection getConnection() {
		return connection;
	}

	public Map<String, String> getProp() {
		return prop;
	}

	public void setProp(Map<String, String> prop) {
		this.prop = prop;
	}

	@Bean("configuration")
	public org.apache.hadoop.conf.Configuration configuration() {
		org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
		Set<String> keySet = prop.keySet();
		for (String key : keySet) {
			configuration.set(key, prop.get(key));
		}
		return configuration;
	}

	@Bean
	public Connection connection(@Qualifier("configuration") org.apache.hadoop.conf.Configuration configuration) {
		// Connection pool
		try {
			connection = ConnectionFactory.createConnection(configuration);
			LOG.info("create connection ok!");
		} catch (IOException e) {
			LOG.error("create connection error", e);
		}
		return connection;
	}

	public TTransport transport() {
		TTransport transport = new TSocket(thriftHost, thriftPort, thriftSocketTimeout, thriftConnectTimeout);
		return transport;
	}

	@PreDestroy
	public void destroy() {
		LOG.info("HBaseConfig destroy!");
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				LOG.error("hbase connection close error", e);
			}
		}
	}

}
