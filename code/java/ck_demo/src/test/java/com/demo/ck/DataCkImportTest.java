package com.demo.ck;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
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

@SpringBootTest
public class DataCkImportTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void testInsert() throws Exception {
        // 导入2000w数据
        DynamicDataSourceContextHolder.push("ck");
        String sql = "INSERT INTO w2000 (c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32,c33 ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream("/home/th/Downloads/data/2000w"), "gbk"));
        List<Object[]> argList = new ArrayList<>();
        int count = 0;
        String line;
        long start = System.currentTimeMillis();
        while ((line = br.readLine()) != null) {
            String[] ss = line.split(",");
            if (ss.length != 33) {
                continue;
            }
            count++;
            argList.add(ss);
            if (argList.size() >= 100000) {
                jdbcTemplate.batchUpdate(sql, argList);
                argList.clear();
                long cost = System.currentTimeMillis() - start;
                System.out.println("cost count=" + count + ",cost=" + cost);
            }
        }
        if (argList.size() > 0) {
            jdbcTemplate.batchUpdate(sql, argList);
        }
    }
}
