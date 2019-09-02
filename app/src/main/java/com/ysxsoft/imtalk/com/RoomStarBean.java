package com.ysxsoft.imtalk.com;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/2 0002
 */
public class RoomStarBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"key_id":1,"id":2279,"uid":21,"icon":"http://chitchat.com/static/admin/img/none.png","nickname":"我是小嗨嗨2689","tt_id":"9554578","now_level":20,"award_gold":"8.00","next_user":"0"}]
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
         * id : 2279
         * uid : 21
         * icon : http://chitchat.com/static/admin/img/none.png
         * nickname : 我是小嗨嗨2689
         * tt_id : 9554578
         * now_level : 20
         * award_gold : 8.00
         * next_user : 0
         */

        private int key_id;
        private int id;
        private String uid;
        private String icon;
        private String nickname;
        private String tt_id;
        private String now_level;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
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

        public String getNow_level() {
            return now_level;
        }

        public void setNow_level(String now_level) {
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
