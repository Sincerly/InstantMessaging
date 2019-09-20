package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class HomeFRoomBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"cids":10,"cname":"男神","roomList":[{"room_id":40,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":42,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":45,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"}]}]
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
         * cids : 10
         * cname : 男神
         * roomList : [{"room_id":40,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":42,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":45,"room_name":"我是小嗨嗨2689","label_name":null,"uid":21,"icon":"http://chitchat.com/static/admin/img/icon.png"}]
         */

        private int cids;
        private String cname;
        private List<RoomListBean> roomList;

        public int getCids() {
            return cids;
        }

        public void setCids(int cids) {
            this.cids = cids;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public List<RoomListBean> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<RoomListBean> roomList) {
            this.roomList = roomList;
        }

        public static class RoomListBean {
            /**
             * room_id : 40
             * room_name : 我是小嗨嗨2689
             * label_name : null
             * uid : 21
             * icon : http://chitchat.com/static/admin/img/icon.png
             */

            private int room_id;
            private String room_name;
            private String label_name;
            private int uid;
            private String icon;

            public String getIs_lock() {
                return is_lock;
            }

            public void setIs_lock(String is_lock) {
                this.is_lock = is_lock;
            }

            private String is_lock;
            public String getMemCount() {
                return memCount;
            }

            public void setMemCount(String memCount) {
                this.memCount = memCount;
            }

            private String memCount;

            public int getRoom_id() {
                return room_id;
            }

            public void setRoom_id(int room_id) {
                this.room_id = room_id;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getLabel_name() {
                return label_name;
            }

            public void setLabel_name(String label_name) {
                this.label_name = label_name;
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
        }
    }
}
