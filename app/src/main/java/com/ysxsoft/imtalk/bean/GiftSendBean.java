package com.ysxsoft.imtalk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftSendBean extends CommonBean{

    /**
     * code : 0
     * data : [{"send_name":"Sincerly","send_icon":"http://chitchat.rhhhyy.com/uploads/images/icon/1567411549860181274.png","sl_name":"会飞的鱼~~","sl_icon":"http://chitchat.rhhhyy.com/uploads/images/icon/1566957193978760738.png","gift_nums":"1","gift_pic":"http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png","is_big_money":1}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * send_name : Sincerly
         * send_icon : http://chitchat.rhhhyy.com/uploads/images/icon/1567411549860181274.png
         * sl_name : 会飞的鱼~~
         * sl_icon : http://chitchat.rhhhyy.com/uploads/images/icon/1566957193978760738.png
         * gift_nums : 1
         * gift_pic : http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png
         * is_big_money : 1
         */

        private String send_name;
        private String send_icon;
        private String sl_name;
        private String sl_icon;
        private String gift_nums;
        private String gift_pic;
        private String is_big_money;

        public String getSend_name() {
            return send_name;
        }

        public void setSend_name(String send_name) {
            this.send_name = send_name;
        }

        public String getSend_icon() {
            return send_icon;
        }

        public void setSend_icon(String send_icon) {
            this.send_icon = send_icon;
        }

        public String getSl_name() {
            return sl_name;
        }

        public void setSl_name(String sl_name) {
            this.sl_name = sl_name;
        }

        public String getSl_icon() {
            return sl_icon;
        }

        public void setSl_icon(String sl_icon) {
            this.sl_icon = sl_icon;
        }

        public String getGift_nums() {
            return gift_nums;
        }

        public void setGift_nums(String gift_nums) {
            this.gift_nums = gift_nums;
        }

        public String getGift_pic() {
            return gift_pic;
        }

        public void setGift_pic(String gift_pic) {
            this.gift_pic = gift_pic;
        }

        public String getIs_big_money() {
            return is_big_money == null ? "" : is_big_money;
        }

        public void setIs_big_money(String is_big_money) {
            this.is_big_money = is_big_money;
        }
    }
}
