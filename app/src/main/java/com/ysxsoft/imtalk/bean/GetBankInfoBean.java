package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
public class GetBankInfoBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"uid":"12","bank_id":5,"user_bank_id":"7","bank_name":"工商银行","real_name":"黑芝麻胡","bank_number":"978456189456","bank_address":"的点点滴滴"}
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
         * uid : 12
         * bank_id : 5
         * user_bank_id : 7
         * bank_name : 工商银行
         * real_name : 黑芝麻胡
         * bank_number : 978456189456
         * bank_address : 的点点滴滴
         */

        private String uid;
        private String bank_id;
        private String user_bank_id;
        private String bank_name;
        private String real_name;
        private String bank_number;
        private String bank_address;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBank_id() {
            return bank_id;
        }

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }

        public String getUser_bank_id() {
            return user_bank_id;
        }

        public void setUser_bank_id(String user_bank_id) {
            this.user_bank_id = user_bank_id;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getBank_number() {
            return bank_number;
        }

        public void setBank_number(String bank_number) {
            this.bank_number = bank_number;
        }

        public String getBank_address() {
            return bank_address;
        }

        public void setBank_address(String bank_address) {
            this.bank_address = bank_address;
        }
    }
}
