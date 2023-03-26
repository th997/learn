package com.demo.es;

import cn.easyes.annotation.rely.FieldType;
import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsIndexWrapper;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.LambdaEsUpdateWrapper;
import cn.easyes.starter.register.EsMapperScan;
import com.demo.es.entity.User;
import com.demo.es.repository.UserESMapper;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.common.settings.Settings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EsMapperScan("com.demo.es.repository")
public class EasyESTest {

    @Autowired
    UserESMapper userESMapper;

    @Test
    void createIndex() throws Exception {
        LambdaEsIndexWrapper<User> wrapper = new LambdaEsIndexWrapper<User>();
        wrapper.indexName("user1");
        wrapper.settings(3, 1);
        //wrapper.join()
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(User.class);
        for (PropertyDescriptor pd : pds) {
            if (pd.getReadMethod() != null && pd.getWriteMethod() != null && pd.getReadMethod().getReturnType().equals(String.class)) {
                wrapper.mapping(pd.getName(), FieldType.KEYWORD);
            }
        }
        userESMapper.createIndex(wrapper);
    }

    @Test
    void pageQuery() throws Exception {
        LambdaEsQueryWrapper<User> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq("c7", "19000101");
        wrapper.likeRight(User::getC1, "刘");
        EsPageInfo<User> page = userESMapper.pageQuery(wrapper, 1, 10);
        System.out.println(page);
        wrapper.size(10);
        List<User> list = userESMapper.selectList(wrapper);
        System.out.println(list);
    }

    @Test
    void selectList() throws Exception {
        LambdaEsQueryWrapper<User> wrapper = new LambdaEsQueryWrapper<>();
        //wrapper.match(User::getC1, "谢小海");
        //wrapper.eq("c1.keyword", "谢小海");
        // wrapper.likeRight("c1.keyword", "谢小");
        wrapper.eq(User::getC1, "邬佳丽1");
        wrapper.likeRight(User::getC1, "邬佳丽");
        wrapper.like(User::getC1, "佳丽");
        wrapper.size(10);
        List<User> list = userESMapper.selectList(wrapper);
        System.out.println(list);
    }


    @Test
    void update() throws Exception {
        LambdaEsUpdateWrapper<User> wrapper = new LambdaEsUpdateWrapper<>();
        wrapper.eq(User::getC1, "邬佳丽");
        User user = new User();
        user.setC1("邬佳丽1");
        Integer af = userESMapper.update(user, wrapper);
        System.out.println(af);
    }


    @Test
    void testInsert() throws Exception {
        // 导入2000w数据
        BufferedReader br = IOUtils.toBufferedReader(new InputStreamReader(new FileInputStream("/f/data/2000w"), "gbk"));
        List<User> users = new ArrayList<>();
        int count = 0;
        String line;
        long start = System.currentTimeMillis();
        while ((line = br.readLine()) != null) {
            String[] ss = line.split(",");
            if (ss.length != 33) {
                //System.out.println(ss);
                continue;
            }
            User user = new User();
            for (int i = 1; i <= 33; i++) {
                BeanUtils.getPropertyDescriptor(User.class, "c" + i).getWriteMethod().invoke(user, ss[i - 1]);
            }
            users.add(user);
            count++;
            if (count < 0) {
                continue;
            }
            if (count % 10000 == 0) {
                //System.out.println(user);
                userESMapper.insertBatch(users);
                users.clear();
                long cost = System.currentTimeMillis() - start;
                System.out.println("cost count=" + count + ",cost=" + cost);
            }
        }
        if (users.size() > 0) {
            userESMapper.insertBatch(users);
        }
    }
}
