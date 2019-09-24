package com.ysxsoft.imtalk.bean;

import java.util.ArrayList;
import java.util.List;

public class EggBean extends CommonBean {


    /**
     * data : {"rid":35,"sg_name":"粉红火箭","aw_pic":"http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png","aw_gif":"http://chitchat.rhhhyy.com/uploads/images/20190826/1b8e43cd206cbbf8ef9c04cbd2cfc3dd.gif"}
     */

    List<DataBean> data;

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rid : 35
         * sg_name : 粉红火箭
         * aw_pic : http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png
         * aw_gif : http://chitchat.rhhhyy.com/uploads/images/20190826/1b8e43cd206cbbf8ef9c04cbd2cfc3dd.gif
         */

        private int rid;
        private String sg_name;
        private String aw_pic;
        private String aw_gif;
        //修改之后字段
        private String sg_gif;//动态
        private String sg_pic;//图片
        private String aw_name;//名称
        private String aw_gold;//金币
        private String sg_gold;//金币
        private String gift_id;//砸金蛋礼物id
        private String is_big_money;//1超过大额
        private String uid;

        public String getSg_gold() {
            return sg_gold == null ? "" : sg_gold;
        }

        public void setSg_gold(String sg_gold) {
            this.sg_gold = sg_gold;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getSg_name() {
            return sg_name;
        }

        public void setSg_name(String sg_name) {
            this.sg_name = sg_name;
        }

        public String getAw_pic() {
            return aw_pic;
        }

        public void setAw_pic(String aw_pic) {
            this.aw_pic = aw_pic;
        }

        public String getAw_gif() {
            return aw_gif;
        }

        public void setAw_gif(String aw_gif) {
            this.aw_gif = aw_gif;
        }

        public String getSg_gif() {
            return sg_gif == null ? "" : sg_gif;
        }

        public void setSg_gif(String sg_gif) {
            this.sg_gif = sg_gif;
        }

        public String getSg_pic() {
            return sg_pic == null ? "" : sg_pic;
        }

        public void setSg_pic(String sg_pic) {
            this.sg_pic = sg_pic;
        }

        public String getAw_name() {
            return aw_name == null ? "" : aw_name;
        }

        public void setAw_name(String aw_name) {
            this.aw_name = aw_name;
        }

        public String getAw_gold() {
            return aw_gold == null ? "" : aw_gold;
        }

        public void setAw_gold(String aw_gold) {
            this.aw_gold = aw_gold;
        }

        public String getGift_id() {
            return gift_id == null ? "" : gift_id;
        }

        public void setGift_id(String gift_id) {
            this.gift_id = gift_id;
        }

        public String getIs_big_money() {
            return is_big_money == null ? "" : is_big_money;
        }

        public void setIs_big_money(String is_big_money) {
            this.is_big_money = is_big_money;
        }

        public String getUid() {
            return uid == null ? "" : uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
