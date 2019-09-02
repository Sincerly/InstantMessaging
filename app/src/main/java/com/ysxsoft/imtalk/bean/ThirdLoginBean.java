package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class ThirdLoginBean {

    /**
     * code : 0
     * msg : 登录成功
     * data : {"userInfo":{"uid":27,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyNyIsImlwIjoiMTI3LjAuMC4xIiwiZXhwaXJlX3RpbWUiOjYwNDgwMCwiaWF0IjoxNTY1NTkwNjk2LCJleHAiOjE1NjYxOTU0OTZ9.ajEzahACelYYdpoOE4bvT7b7OTkwhXkzRsG7_ccrprA","chat_token":"ZwPInPZ4txrWASi3H0gBMOOl9YBJNT+ozWTqIy2ZJPyQFxVLiWDLNQJcb3BJh7Pyu/lEPyrm0LvTlyMU2yVRng=="}}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userInfo : {"uid":27,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyNyIsImlwIjoiMTI3LjAuMC4xIiwiZXhwaXJlX3RpbWUiOjYwNDgwMCwiaWF0IjoxNTY1NTkwNjk2LCJleHAiOjE1NjYxOTU0OTZ9.ajEzahACelYYdpoOE4bvT7b7OTkwhXkzRsG7_ccrprA","chat_token":"ZwPInPZ4txrWASi3H0gBMOOl9YBJNT+ozWTqIy2ZJPyQFxVLiWDLNQJcb3BJh7Pyu/lEPyrm0LvTlyMU2yVRng=="}
         */

        private UserInfoBean userInfo;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * uid : 27
             * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyNyIsImlwIjoiMTI3LjAuMC4xIiwiZXhwaXJlX3RpbWUiOjYwNDgwMCwiaWF0IjoxNTY1NTkwNjk2LCJleHAiOjE1NjYxOTU0OTZ9.ajEzahACelYYdpoOE4bvT7b7OTkwhXkzRsG7_ccrprA
             * chat_token : ZwPInPZ4txrWASi3H0gBMOOl9YBJNT+ozWTqIy2ZJPyQFxVLiWDLNQJcb3BJh7Pyu/lEPyrm0LvTlyMU2yVRng==
             */

            private int uid;
            private String token;
            private String chat_token;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getChat_token() {
                return chat_token;
            }

            public void setChat_token(String chat_token) {
                this.chat_token = chat_token;
            }
        }
    }
}
