package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/20 0020
 */
public class RoomMemListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"roomUserList":[{"uid":"24","nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.sanzhima.cn/uploads/images/20190731/1548406553724_6139275.jpg","ml_level":1,"user_level":1,"sort":0,"is_room":1,"is_admin":0}],"count":1}
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
         * roomUserList : [{"uid":"24","nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.sanzhima.cn/uploads/images/20190731/1548406553724_6139275.jpg","ml_level":1,"user_level":1,"sort":0,"is_room":1,"is_admin":0}]
         * count : 1
         */

        private String count;
        private List<RoomUserListBean> roomUserList;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<RoomUserListBean> getRoomUserList() {
            return roomUserList;
        }

        public void setRoomUserList(List<RoomUserListBean> roomUserList) {
            this.roomUserList = roomUserList;
        }

        public static class RoomUserListBean {
            /**
             * uid : 24
             * nickname : 黑芝麻胡
             * sex : 1
             * icon : http://chitchat.sanzhima.cn/uploads/images/20190731/1548406553724_6139275.jpg
             * ml_level : 1
             * user_level : 1
             * sort : 0
             * is_room : 1
             * is_admin : 0
             */

            private String uid;
            private String nickname;
            private int sex;
            private String icon;
            private String ml_level;
            private String user_level;
            private int sort;
            private String is_room;
            private String is_admin;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
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

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getIs_room() {
                return is_room;
            }

            public void setIs_room(String is_room) {
                this.is_room = is_room;
            }

            public String getIs_admin() {
                return is_admin;
            }

            public void setIs_admin(String is_admin) {
                this.is_admin = is_admin;
            }
        }
    }
}
