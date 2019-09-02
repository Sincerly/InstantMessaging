package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class QdSignListBean {

    /**
     * code : 0
     * mdg : 获取成功
     * data : {"sign_list":[{"sign_id":"1","day":"2019-07-17","qd":1,"lw":1,"sign_gold":5,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第一天"},{"sign_id":"2","day":"2019-07-18","qd":0,"lw":1,"sign_gold":10,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第二天"},{"sign_id":"3","day":"2019-07-19","qd":0,"lw":3,"sign_gold":0,"lw_name":"兔子头饰","lw_pic":"http://chitchat.com/uploads/images/20190708/d3a7ccf5eb42f58a6fb032672a62c1a5.png","day_name":"第三天"},{"sign_id":"4","day":"2019-07-20","qd":0,"lw":1,"sign_gold":20,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第四天"},{"sign_id":"5","day":"2019-07-21","qd":0,"lw":1,"sign_gold":25,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第五天"},{"sign_id":"6","day":"2019-07-22","qd":"1","lw":1,"sign_gold":30,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第六天"},{"sign_id":"7","day":"2019-07-23","qd":2,"lw":2,"sign_gold":0,"lw_name":"灰机","lw_pic":"http://chitchat.com/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png","day_name":"第七天"}],"sign_day":3,"sign_descs":"今天还没有签到哦~"}
     */

    private int code;
    private String mdg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMdg() {
        return mdg;
    }

    public void setMdg(String mdg) {
        this.mdg = mdg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sign_list : [{"sign_id":"1","day":"2019-07-17","qd":1,"lw":1,"sign_gold":5,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第一天"},{"sign_id":"2","day":"2019-07-18","qd":0,"lw":1,"sign_gold":10,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第二天"},{"sign_id":"3","day":"2019-07-19","qd":0,"lw":3,"sign_gold":0,"lw_name":"兔子头饰","lw_pic":"http://chitchat.com/uploads/images/20190708/d3a7ccf5eb42f58a6fb032672a62c1a5.png","day_name":"第三天"},{"sign_id":"4","day":"2019-07-20","qd":0,"lw":1,"sign_gold":20,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第四天"},{"sign_id":"5","day":"2019-07-21","qd":0,"lw":1,"sign_gold":25,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第五天"},{"sign_id":"6","day":"2019-07-22","qd":"1","lw":1,"sign_gold":30,"lw_name":"金币","lw_pic":"http://chitchat.com/static/admin/img/jinbi.png","day_name":"第六天"},{"sign_id":"7","day":"2019-07-23","qd":2,"lw":2,"sign_gold":0,"lw_name":"灰机","lw_pic":"http://chitchat.com/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png","day_name":"第七天"}]
         * sign_day : 3
         * sign_descs : 今天还没有签到哦~
         */

        private String sign_day;
        private String sign_descs;
        private List<SignListBean> sign_list;

        public String getSign_day() {
            return sign_day;
        }

        public void setSign_day(String sign_day) {
            this.sign_day = sign_day;
        }

        public String getSign_descs() {
            return sign_descs;
        }

        public void setSign_descs(String sign_descs) {
            this.sign_descs = sign_descs;
        }

        public List<SignListBean> getSign_list() {
            return sign_list;
        }

        public void setSign_list(List<SignListBean> sign_list) {
            this.sign_list = sign_list;
        }

        public static class SignListBean {
            /**
             * sign_id : 1
             * day : 2019-07-17
             * qd : 1
             * lw : 1
             * sign_gold : 5
             * lw_name : 金币
             * lw_pic : http://chitchat.com/static/admin/img/jinbi.png
             * day_name : 第一天
             */

            private String sign_id;
            private String day;
            private int qd;
            private int lw;
            private int sign_gold;
            private String lw_name;
            private String lw_pic;
            private String day_name;

            public String getSign_id() {
                return sign_id;
            }

            public void setSign_id(String sign_id) {
                this.sign_id = sign_id;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public int getQd() {
                return qd;
            }

            public void setQd(int qd) {
                this.qd = qd;
            }

            public int getLw() {
                return lw;
            }

            public void setLw(int lw) {
                this.lw = lw;
            }

            public int getSign_gold() {
                return sign_gold;
            }

            public void setSign_gold(int sign_gold) {
                this.sign_gold = sign_gold;
            }

            public String getLw_name() {
                return lw_name;
            }

            public void setLw_name(String lw_name) {
                this.lw_name = lw_name;
            }

            public String getLw_pic() {
                return lw_pic;
            }

            public void setLw_pic(String lw_pic) {
                this.lw_pic = lw_pic;
            }

            public String getDay_name() {
                return day_name;
            }

            public void setDay_name(String day_name) {
                this.day_name = day_name;
            }
        }
    }
}
