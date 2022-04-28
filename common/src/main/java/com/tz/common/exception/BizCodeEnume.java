package com.tz.common.exception;
/**
 * 错误码和错误信息定义
 * 1.错误码定义规则为5位数
 * 2.前2位标识业务场景，后3位标识错误码。例如：100001.10:通用 001：系统未知错误
 * 3.维护错误码后需要维护错误描述，定义为枚举形式
 * 错误码列表
 * 10: 通用
 *   001：参数格式校验
 * 11: 商品
 * 12: 订单
 * 13: 购物车
 * 14: 物流
 * */
public enum BizCodeEnume {
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    VALID_EXCEPTION(10001,"参数格式校验异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    LOGINACCT_PASSWORD_INVALID_EXCEPTION(15003,"账号密码错误"),
    NO_STOCK_EXCPTION(21000,"no stock");
    private Integer code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
