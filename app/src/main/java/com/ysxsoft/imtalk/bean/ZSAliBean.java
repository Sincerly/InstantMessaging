package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class ZSAliBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : {"rs":{"uid":"54","type":1,"diamond":"0.00","blv":"1.00","money":0,"account_number":null,"account_name":null}}
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
         * rs : {"uid":"54","type":1,"diamond":"0.00","blv":"1.00","money":0,"account_number":null,"account_name":null}
         */

        private RsBean rs;

        public RsBean getRs() {
            return rs;
        }

        public void setRs(RsBean rs) {
            this.rs = rs;
        }

        public static class RsBean {
            /**
             * uid : 54
             * type : 1
             * diamond : 0.00
             * blv : 1.00
             * money : 0
             * account_number : null
             * account_name : null
             */

            private String uid;
            private int type;
            private String diamond;
            private String blv;
            private String money;
            private String account_number;
            private String account_name;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDiamond() {
                return diamond;
            }

            public void setDiamond(String diamond) {
                this.diamond = diamond;
            }

            public String getBlv() {
                return blv;
            }

            public void setBlv(String blv) {
                this.blv = blv;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getAccount_number() {
                return account_number;
            }

            public void setAccount_number(String account_number) {
                this.account_number = account_number;
            }

            public String getAccount_name() {
                return account_name;
            }

            public void setAccount_name(String account_name) {
                this.account_name = account_name;
            }
        }
    }
}
