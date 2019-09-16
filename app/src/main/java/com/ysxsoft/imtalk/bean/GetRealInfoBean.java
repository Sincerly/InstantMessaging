package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/12 0012
 */
public class GetRealInfoBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"uid":2,"is_real":1,"number_id":"410221199404253428","username":"Amor_L"}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
         * uid : 2
         * is_real : 1
         * number_id : 410221199404253428
         * username : Amor_L
         */

        private int uid;
        private int is_real;
        private String number_id;
        private String username;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getIs_real() {
            return is_real;
        }

        public void setIs_real(int is_real) {
            this.is_real = is_real;
        }

        public String getNumber_id() {
            return number_id;
        }

        public void setNumber_id(String number_id) {
            this.number_id = number_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
