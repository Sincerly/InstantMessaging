package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/22 0022
 */
public class RoomMwUserBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"uid":21,"tt_id":"9554578","nickname":"我是小嗨嗨2689","icon":"http://chitchat.com/static/admin/img/icon.png","fmy_name":"吃货家族","ml_level":1,"user_level":20}
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
         * tt_id : 9554578
         * nickname : 我是小嗨嗨2689
         * icon : http://chitchat.com/static/admin/img/icon.png
         * fmy_name : 吃货家族
         * ml_level : 1
         * user_level : 20
         */

        private int uid;
        private String tt_id;
        private String nickname;
        private String icon;
        private String fmy_name;
        private String ml_level;
        private String user_level;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        private String sex;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getTt_id() {
            return tt_id;
        }

        public void setTt_id(String tt_id) {
            this.tt_id = tt_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getFmy_name() {
            return fmy_name;
        }

        public void setFmy_name(String fmy_name) {
            this.fmy_name = fmy_name;
        }

        public String getMl_level() {
            return ml_level;
        }

        public void setMl_level(String ml_level) {
            this.ml_level = ml_level;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }
    }
}
