package com.tz.common.constant;

public class ProductConstant {
    public enum AttrEnum{
        ATTR_BASE_TYPE(1,"基本属性"),ATTR_SALE_TYPE(0,"销售属性");
        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }
    }


}

