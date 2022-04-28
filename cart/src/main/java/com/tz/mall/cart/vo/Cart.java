package com.tz.mall.cart.vo;


import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/** 封装购物车整体信息*/

@ToString
public class Cart {

    private List<CartItem> items;
    private Integer countNum;  //购物车里的商品总数量
    private Integer countType; //购物车里商品的类型数量
    private BigDecimal totalAmount; //购物车里所有商品的总价
    private BigDecimal reduce=new BigDecimal("0.00");//减免的价格（优惠）
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

//    累加商品所有数量
    public Integer getCountNum() {
        Integer count=0;
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                count+=item.getCount();
            }
        }
        return count;
    }
    //    累加商品所有类型数量
    public Integer getCountType() {
        Integer count=0;
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                count+=1;
            }
        }
        return count;
    }



    public BigDecimal getTotalAmount() {
        BigDecimal amount=new BigDecimal("0.00");
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                if(item.getCheck()){
                    BigDecimal totalPrice = item.getTotalPrice();
                    amount=amount.add(totalPrice);
                }

            }
        }
//        减去优惠信息
        BigDecimal subtract = amount.subtract(getReduce());
        return subtract;
    }



    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }



}
