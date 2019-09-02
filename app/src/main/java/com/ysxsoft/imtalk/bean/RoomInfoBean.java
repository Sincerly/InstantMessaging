package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/31 0031
 */
public class RoomInfoBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"room_name":"我是小嗨嗨2689","room_num":"2192156","room_notice":"房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告","room_label":"暂无","room_desc":null,"room_content":null,"is_lock":0,"lock_pwd":null,"room_bg":"http://chitchat.com/static/admin/img/none.png","room_gift_tx":1,"room_is_fair":1,"room_pure":0}
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
         * room_name : 我是小嗨嗨2689
         * room_num : 2192156
         * room_notice : 房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告
         * room_label : 暂无
         * room_desc : null
         * room_content : null
         * is_lock : 0
         * lock_pwd : null
         * room_bg : http://chitchat.com/static/admin/img/none.png
         * room_gift_tx : 1
         * room_is_fair : 1
         * room_pure : 0
         */

        private String room_name;
        private String room_num;
        private String room_notice;
        private String room_label;
        private String room_desc;
        private String room_content;
        private int is_lock;
        private String lock_pwd;
        private String room_bg;
        private int room_gift_tx;
        private int room_is_fair;
        private int room_pure;
        private String uid;//创建房间人的Id
        private String memCount;

        public String getMemCount() {
            return memCount;
        }

        public void setMemCount(String memCount) {
            this.memCount = memCount;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        private String room_id;

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_num() {
            return room_num;
        }

        public void setRoom_num(String room_num) {
            this.room_num = room_num;
        }

        public String getRoom_notice() {
            return room_notice;
        }

        public void setRoom_notice(String room_notice) {
            this.room_notice = room_notice;
        }

        public String getRoom_label() {
            return room_label;
        }

        public void setRoom_label(String room_label) {
            this.room_label = room_label;
        }

        public String getRoom_desc() {
            return room_desc;
        }

        public void setRoom_desc(String room_desc) {
            this.room_desc = room_desc;
        }

        public String getRoom_content() {
            return room_content;
        }

        public void setRoom_content(String room_content) {
            this.room_content = room_content;
        }

        public int getIs_lock() {
            return is_lock;
        }

        public void setIs_lock(int is_lock) {
            this.is_lock = is_lock;
        }

        public String getLock_pwd() {
            return lock_pwd;
        }

        public void setLock_pwd(String lock_pwd) {
            this.lock_pwd = lock_pwd;
        }

        public String getRoom_bg() {
            return room_bg;
        }

        public void setRoom_bg(String room_bg) {
            this.room_bg = room_bg;
        }

        public int getRoom_gift_tx() {
            return room_gift_tx;
        }

        public void setRoom_gift_tx(int room_gift_tx) {
            this.room_gift_tx = room_gift_tx;
        }

        public int getRoom_is_fair() {
            return room_is_fair;
        }

        public void setRoom_is_fair(int room_is_fair) {
            this.room_is_fair = room_is_fair;
        }

        public int getRoom_pure() {
            return room_pure;
        }

        public void setRoom_pure(int room_pure) {
            this.room_pure = room_pure;
        }
    }
}
