package com.tz.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    private String catalog1Id; //一级父分类Id
    private List<Catelog3Vo> catalog3List; //三级子分类
    private String id;
    private String name;



    /**三级分类vo*/
    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id;
        private String id;
        private String name;
    }
}
