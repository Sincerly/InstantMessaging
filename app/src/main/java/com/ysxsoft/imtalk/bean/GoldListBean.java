package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class GoldListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":14,"gd_pic":"http://chitchat.com/uploads/images/20190709/e7c63328bc5a37be4434a8d2a0435a37.png","gd_gold_num":"10.00","gd_money":"1.00"}]
     */

    private int code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 14
         * gd_pic : http://chitchat.com/uploads/images/20190709/e7c63328bc5a37be4434a8d2a0435a37.png
         * gd_gold_num : 10.00
         * gd_money : 1.00
         */

        private int id;
        private String gd_pic;
        private String gd_gold_num;
        private String gd_money;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGd_pic() {
            return gd_pic;
        }

        public void setGd_pic(String gd_pic) {
            this.gd_pic = gd_pic;
        }

        public String getGd_gold_num() {
            return gd_gold_num;
        }

        public void setGd_gold_num(String gd_gold_num) {
            this.gd_gold_num = gd_gold_num;
        }

        public String getGd_money() {
            return gd_money;
        }

        public void setGd_money(String gd_money) {
            this.gd_money = gd_money;
        }
    }
}
