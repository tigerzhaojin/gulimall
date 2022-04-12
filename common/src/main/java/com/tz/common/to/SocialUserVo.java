package com.tz.common.to;

import lombok.Data;
import lombok.ToString;

@Data
public class SocialUserVo {

    @Data
    @ToString
    public static class GitVo{
        private String access_token;
        private String token_type;
        private String scope;
        private String uid;
        private String uname;

    }
}
