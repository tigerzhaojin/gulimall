package com.tz.mall.ware.exception;

public class NoStockException extends RuntimeException {
    private Long skuId;
    public NoStockException(Long skuId){
        super(skuId+" No Stock");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
