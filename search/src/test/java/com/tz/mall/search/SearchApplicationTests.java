package com.tz.mall.search;


import com.alibaba.fastjson.JSON;
import com.tz.mall.search.config.MallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;
/**
 * 测试Index*/
    @Test
    public void indexData() throws IOException {

        IndexRequest indexRequest = new IndexRequest("users");
//        封装request
        indexRequest.id("1");
//        indexRequest.source("userName","chris","age",6,"gender","female");
        User user = new User();
        user.setUserName("chris.zhao");
        user.setAge(6);
        user.setGender("femail");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);
//        执行保存操作
        IndexResponse response =
                client.index(indexRequest, MallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(response);
    }

    @Data
    class User{
        String userName;
        Integer age;
        String gender;
    }
    @Test
    public void contextLoads() {

        System.out.println("当前的highLevelClient......: "+client);
    }

}
