package com.demo.flink;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

public class LocalTest {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();
        DataSet<String> data = env.readTextFile("file:///home/th/Downloads/data/2000w", "gbk");

        data.filter(new FilterFunction<String>() {
            public boolean filter(String value) {
                return value.contains("19900123");
            }
        }).writeAsText("file:///home/th/Downloads/data/2000w_result");

        JobExecutionResult res = env.execute();
    }
}
