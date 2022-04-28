package com.tz.mall.ware.config;


import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@EnableTransactionManagement
@MapperScan(basePackages = "com.tz.mall.ware.dao")
public class MybatisConfig {
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
//        paginationInnerInterceptor.setOverflow(true);
//        paginationInnerInterceptor.setMaxLimit(1000L);
        return paginationInnerInterceptor;
    }

//    @Autowired
//    DataSourceProperties dataSourceProperties;

//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties){
//
//        HikariDataSource dataSource =
//                dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//        if (StringUtils.hasText(dataSourceProperties.getName())){
//            dataSource.setPoolName(dataSourceProperties.getName());
//        }
//        return new DataSourceProxy(dataSource);
//    }
}
