package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class RefundListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"uid":"21","tx_status":0,"type":1,"money":"-10.00","desc":"","add_time":"2019-08-10 11:55:21"},{"uid":"21","tx_status":0,"type":2,"money":"-10.00","desc":"","add_time":"2019-08-05 11:03:19"}]
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
         * uid : 21
         * tx_status : 0
         * type : 1
         * money : -10.00
         * desc :
         * add_time : 2019-08-10 11:55:21
         */

        private String uid;
        private int tx_status;
        private int type;
        private String money;
        private String desc;
        private String add_time;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getTx_status() {
            return tx_status;
        }

        public void setTx_status(int tx_status) {
            this.tx_status = tx_status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
