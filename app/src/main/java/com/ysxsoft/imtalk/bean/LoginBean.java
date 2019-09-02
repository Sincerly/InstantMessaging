package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class LoginBean {

    /**
     * code : 0
     * msg : 登陆成功！
     * data : {"uid":14,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjE0LCJpcCI6IjEyNy4wLjAuMSIsImV4cGlyZV90aW1lIjo2MDQ4MDAsImlhdCI6MTU2MzYxMzM4OSwiZXhwIjoxNTY0MjE4MTg5fQ.EhklEyr42wWxQc6XIpZa9F7JYuWeID8QtwT5xY4i8PM","chat_token":"UnYcaCHWTXO1418nZ03TFBDw4To6PyHctmuPzkEnAoeY5bUp9WCwhEOSK/0otoHCx1/Y5AevHW1RRyyzkNmicw=="}
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
         * uid : 14
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjE0LCJpcCI6IjEyNy4wLjAuMSIsImV4cGlyZV90aW1lIjo2MDQ4MDAsImlhdCI6MTU2MzYxMzM4OSwiZXhwIjoxNTY0MjE4MTg5fQ.EhklEyr42wWxQc6XIpZa9F7JYuWeID8QtwT5xY4i8PM
         * chat_token : UnYcaCHWTXO1418nZ03TFBDw4To6PyHctmuPzkEnAoeY5bUp9WCwhEOSK/0otoHCx1/Y5AevHW1RRyyzkNmicw==
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
