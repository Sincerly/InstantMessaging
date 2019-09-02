package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
public class UserBankListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"bank_id":1,"uid":"2","bank_name":"农业银行","real_name":"Amor_L","bank_number":"****  ****  ****  0011"},{"bank_id":2,"uid":"2","bank_name":"建设银行","real_name":"Amor_L1","bank_number":"****  ****  ****  0022"},{"bank_id":3,"uid":"2","bank_name":"建设银行","real_name":"Amor_L","bank_number":"****  ****  ****  0022"}]
     * last_page : 1
     */

    private int code;
    private String msg;
    private int last_page;
    private List<DataBean> data;

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

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bank_id : 1
         * uid : 2
         * bank_name : 农业银行
         * real_name : Amor_L
         * bank_number : ****  ****  ****  0011
         */

        private int bank_id;
        private String uid;
        private String bank_name;
        private String real_name;
        private String bank_number;

        public String getBank_address() {
            return bank_address;
        }

        public void setBank_address(String bank_address) {
            this.bank_address = bank_address;
        }

        private String bank_address;

        public int getBank_id() {
            return bank_id;
        }

        public void setBank_id(int bank_id) {
            this.bank_id = bank_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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
    }
}
