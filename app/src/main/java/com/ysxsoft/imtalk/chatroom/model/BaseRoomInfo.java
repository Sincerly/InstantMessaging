package com.ysxsoft.imtalk.chatroom.model;


/**
 * 获取房间列表请求中房间信息
 */
public class BaseRoomInfo {

    private String userCount;
    private RoomInfo roomInfo;

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public static class RoomInfo {
        private String uid;
        private String room_id;
        private String room_name;
        private String add_time;
        private String room_num;
        private String room_notice;
        private String room_label;
        private String room_desc;
        private String room_content;
        private String is_lock;
        private String lock_pwd;
        private String room_bg;
        private String room_gift_tx;
        private String room_is_fair;
        private String room_pure;
        private String nickname;
        private String sex;
        private String gifts;
        private String is_wheat;
        private String is_oc_wheat;

        public String getUser_ts() {
            return user_ts;
        }

        public void setUser_ts(String user_ts) {
            this.user_ts = user_ts;
        }

        private String user_ts;
        private int memCount;

        public String getIs_oc_wheat() {
            return is_oc_wheat;
        }

        public void setIs_oc_wheat(String is_oc_wheat) {
            this.is_oc_wheat = is_oc_wheat;
        }

        public String getGifts() {
            return gifts;
        }

        public void setGifts(String gifts) {
            this.gifts = gifts;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        private String icon;

        public String getIs_wheat() {
            return is_wheat;
        }

        public void setIs_wheat(String is_wheat) {
            this.is_wheat = is_wheat;
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

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
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

        public String getIs_lock() {
            return is_lock;
        }

        public void setIs_lock(String is_lock) {
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

        public String getRoom_gift_tx() {
            return room_gift_tx;
        }

        public void setRoom_gift_tx(String room_gift_tx) {
            this.room_gift_tx = room_gift_tx;
        }

        public String getRoom_is_fair() {
            return room_is_fair;
        }

        public void setRoom_is_fair(String room_is_fair) {
            this.room_is_fair = room_is_fair;
        }

        public String getRoom_pure() {
            return room_pure;
        }

        public void setRoom_pure(String room_pure) {
            this.room_pure = room_pure;
        }

        public int getMemCount() {
            return memCount;
        }

        public void setMemCount(int memCount) {
            this.memCount = memCount;
        }
    }
}
