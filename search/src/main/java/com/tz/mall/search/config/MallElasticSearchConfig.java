package com.tz.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MallElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }
    @Bean
    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder=null;
        builder=RestClient.builder(new HttpHost("101.35.249.158",9200,"http"));
        RestHighLevelClient client=new RestHighLevelClient(builder);
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("http://101.35.249.158/", 9200, "http")));
        return client;
    }
}
