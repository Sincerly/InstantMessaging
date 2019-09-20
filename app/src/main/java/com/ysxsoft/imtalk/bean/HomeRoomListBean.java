package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class HomeRoomListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"pid":1,"cids":2,"cname":"热门推荐","roomList":[{"room_id":39,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":43,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":46,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"}]},{"pid":1,"cids":3,"cname":"官方推荐","roomList":[{"room_id":38,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"}]}]
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
         * pid : 1
         * cids : 2
         * cname : 热门推荐
         * roomList : [{"room_id":39,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":43,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"},{"room_id":46,"room_name":"我是小嗨嗨2689","label_name":null,"icon":"http://chitchat.com/static/admin/img/icon.png"}]
         */

        private int pid;
        private String cids;
        private String cname;
        private List<RoomListBean> roomList;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getCids() {
            return cids;
        }

        public void setCids(String cids) {
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
             * room_id : 39
             * room_name : 我是小嗨嗨2689
             * label_name : null
             * icon : http://chitchat.com/static/admin/img/icon.png
             */

            private int room_id;
            private String room_name;
            private String label_name;
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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
