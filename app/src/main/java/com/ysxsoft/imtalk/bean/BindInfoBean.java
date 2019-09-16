package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/12 0012
 */
public class BindInfoBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"mobile":"15039037666","mobile_bind":1,"zfb_accounts":"123456","zfb_name":"123456","zfb_bind":1}
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
         * mobile : 15039037666
         * mobile_bind : 1
         * zfb_accounts : 123456
         * zfb_name : 123456
         * zfb_bind : 1
         */

        private String mobile;
        private int mobile_bind;
        private String zfb_accounts;
        private String zfb_name;
        private int zfb_bind;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMobile_bind() {
            return mobile_bind;
        }

        public void setMobile_bind(int mobile_bind) {
            this.mobile_bind = mobile_bind;
        }

        public String getZfb_accounts() {
            return zfb_accounts;
        }

        public void setZfb_accounts(String zfb_accounts) {
            this.zfb_accounts = zfb_accounts;
        }

        public String getZfb_name() {
            return zfb_name;
        }

        public void setZfb_name(String zfb_name) {
            this.zfb_name = zfb_name;
        }

        public int getZfb_bind() {
            return zfb_bind;
        }

        public void setZfb_bind(int zfb_bind) {
            this.zfb_bind = zfb_bind;
        }
    }
}
