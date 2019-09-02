package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/16 0016
 */
public class SearchBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"uid":20,"nickname":"Amor_LL","tt_id":"34087456","sex":null,"icon":"http://chitchat.com/static/admin/img/icon.png","ml_level":1,"th_level":1}]
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
         * uid : 20
         * nickname : Amor_LL
         * tt_id : 34087456
         * sex : null
         * icon : http://chitchat.com/static/admin/img/icon.png
         * ml_level : 1
         * th_level : 1
         */

        private int uid;
        private String nickname;
        private String tt_id;
        private String sex;
        private String icon;
        private String ml_level;
        private String th_level;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTt_id() {
            return tt_id;
        }

        public void setTt_id(String tt_id) {
            this.tt_id = tt_id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getMl_level() {
            return ml_level;
        }

        public void setMl_level(String ml_level) {
            this.ml_level = ml_level;
        }

        public String getTh_level() {
            return th_level;
        }

        public void setTh_level(String th_level) {
            this.th_level = th_level;
        }
    }
}
