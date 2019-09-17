package com.ysxsoft.imtalk.bean;

import java.util.List;

public class SupperStarBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"key_id":1,"id":45,"uid":9,"icon":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","nickname":"李四","tt_id":"38720703","now_level":1,"award_gold":"10.00","next_user":"0"}]
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
         * key_id : 1
         * id : 45
         * uid : 9
         * icon : http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
         * nickname : 李四
         * tt_id : 38720703
         * now_level : 1
         * award_gold : 10.00
         * next_user : 0
         */

        private int key_id;
        private int id;
        private int uid;
        private String icon;
        private String nickname;
        private String tt_id;
        private int now_level;
        private String award_gold;
        private String next_user;

        public int getKey_id() {
            return key_id;
        }

        public void setKey_id(int key_id) {
            this.key_id = key_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public int getNow_level() {
            return now_level;
        }

        public void setNow_level(int now_level) {
            this.now_level = now_level;
        }

        public String getAward_gold() {
            return award_gold;
        }

        public void setAward_gold(String award_gold) {
            this.award_gold = award_gold;
        }

        public String getNext_user() {
            return next_user;
        }

        public void setNext_user(String next_user) {
            this.next_user = next_user;
        }
    }
}
