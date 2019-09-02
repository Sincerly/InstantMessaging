package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class ZSBankBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"uid":"21","type":2,"diamond":"60.00","blv":"1.00","money":60,"bank_name":"招商银行（0011）"}
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
         * uid : 21
         * type : 2
         * diamond : 60.00
         * blv : 1.00
         * money : 60
         * bank_name : 招商银行（0011）
         */

        private String uid;
        private int type;
        private String diamond;
        private String blv;
        private String money;
        private String bank_name;

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

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }
    }
}
