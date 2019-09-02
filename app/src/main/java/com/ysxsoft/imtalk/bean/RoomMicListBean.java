package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/31 0031
 */
public class RoomMicListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"mw_id":73,"room_id":37,"uid":21,"icon":"http://chitchat.com/static/admin/img/none.png","sex":2,"sort":0,"nickname":"我是小嗨嗨2689","is_room":1,"is_admin":0,"is_block":0,"is_wheat":1,"is_lock_wheat":1},{"mw_id":74,"room_id":37,"uid":0,"icon":"http://chitchat.com/static/admin/img/none.png","sex":null,"sort":1,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1}]
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
         * mw_id : 73
         * room_id : 37
         * uid : 21
         * icon : http://chitchat.com/static/admin/img/none.png
         * sex : 2
         * sort : 0
         * nickname : 我是小嗨嗨2689
         * is_room : 1
         * is_admin : 0
         * is_block : 0
         * is_wheat : 1
         * is_lock_wheat : 1
         */

        private int mw_id;
        private String room_id;
        private int uid;
        private String icon;
        private int sex;
        private int sort;
        private String nickname;
        private int is_room;
        private int is_admin;
        private int is_block;
        private int is_wheat;
        private int is_lock_wheat;

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        private boolean isChoosed;

        public int getMw_id() {
            return mw_id;
        }

        public void setMw_id(int mw_id) {
            this.mw_id = mw_id;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_room() {
            return is_room;
        }

        public void setIs_room(int is_room) {
            this.is_room = is_room;
        }

        public int getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(int is_admin) {
            this.is_admin = is_admin;
        }

        public int getIs_block() {
            return is_block;
        }

        public void setIs_block(int is_block) {
            this.is_block = is_block;
        }

        public int getIs_wheat() {
            return is_wheat;
        }

        public void setIs_wheat(int is_wheat) {
            this.is_wheat = is_wheat;
        }

        public int getIs_lock_wheat() {
            return is_lock_wheat;
        }

        public void setIs_lock_wheat(int is_lock_wheat) {
            this.is_lock_wheat = is_lock_wheat;
        }
    }
}
