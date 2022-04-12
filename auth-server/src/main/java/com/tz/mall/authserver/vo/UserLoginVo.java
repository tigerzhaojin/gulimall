package com.tz.mall.authserver.vo;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLoginVo {
    private String loginAcct;
    private String passWord;
}
