package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/30 0030
 */
public class BlackListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"id":2,"room_id":37,"uid":21,"icon":"http://chitchat.com/static/admin/img/none.png","nickname":"我是小嗨嗨2689"}]
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
         * id : 2
         * room_id : 37
         * uid : 21
         * icon : http://chitchat.com/static/admin/img/none.png
         * nickname : 我是小嗨嗨2689
         */

        private int id;
        private String room_id;
        private String uid;
        private String icon;
        private String nickname;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
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
    }
}
