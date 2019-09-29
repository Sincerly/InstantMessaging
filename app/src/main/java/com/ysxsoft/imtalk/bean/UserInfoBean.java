package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class UserInfoBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"uid":"12","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","nickname":"测试账号1","sex":null,"tt_id":"07329643","date_birth":"2019-07-20","user_desc":"123456","fans":0,"gzrs":0,"user_level":1,"charm_level":1,"picture":[{"id":2,"user_picture_id":"26","photo":"http://chitchat.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png"},{"id":3,"user_picture_id":"26","photo":"http://chitchat.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png"},{"id":4,"user_picture_id":"36","photo":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}]}
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
         * uid : 12
         * icon : http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
         * nickname : 测试账号1
         * sex : null
         * tt_id : 07329643
         * date_birth : 2019-07-20
         * user_desc : 123456
         * fans : 0
         * gzrs : 0
         * user_level : 1
         * charm_level : 1
         * picture : [{"id":2,"user_picture_id":"26","photo":"http://chitchat.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png"},{"id":3,"user_picture_id":"26","photo":"http://chitchat.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png"},{"id":4,"user_picture_id":"36","photo":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}]
         */

        private String uid;
        private String icon;
        private String nickname;
        private String sex;
        private String tt_id;
        private String date_birth;
        private String user_desc;
        private String roomId;
        private String money;
        private String diamond;
        private String now_roomId;
        private int fans;
        private int gzrs;
        private int user_level;
        private int charm_level;
        private List<PictureBean> picture;
        private String user_zj_name;//座驾名称
        private String user_zj_pic;//座驾图片
        private String user_ts_name;//t头饰名称

        public String getUser_ts_name() {
            return user_ts_name;
        }

        public void setUser_ts_name(String user_ts_name) {
            this.user_ts_name = user_ts_name;
        }

        public String getUser_ts_pic() {
            return user_ts_pic;
        }

        public void setUser_ts_pic(String user_ts_pic) {
            this.user_ts_pic = user_ts_pic;
        }

        private String user_ts_pic;//t头饰图片

        public String getUser_zj_name() {
            return user_zj_name == null ? "" : user_zj_name;
        }

        public void setUser_zj_name(String user_zj_name) {
            this.user_zj_name = user_zj_name;
        }

        public String getUser_zj_pic() {
            return user_zj_pic == null ? "" : user_zj_pic;
        }

        public void setUser_zj_pic(String user_zj_pic) {
            this.user_zj_pic = user_zj_pic;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getNow_roomId() {
            return now_roomId;
        }

        public void setNow_roomId(String now_roomId) {
            this.now_roomId = now_roomId;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTt_id() {
            return tt_id;
        }

        public void setTt_id(String tt_id) {
            this.tt_id = tt_id;
        }

        public String getDate_birth() {
            return date_birth;
        }

        public void setDate_birth(String date_birth) {
            this.date_birth = date_birth;
        }

        public String getUser_desc() {
            return user_desc;
        }

        public void setUser_desc(String user_desc) {
            this.user_desc = user_desc;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getGzrs() {
            return gzrs;
        }

        public void setGzrs(int gzrs) {
            this.gzrs = gzrs;
        }

        public int getUser_level() {
            return user_level;
        }

        public void setUser_level(int user_level) {
            this.user_level = user_level;
        }

        public int getCharm_level() {
            return charm_level;
        }

        public void setCharm_level(int charm_level) {
            this.charm_level = charm_level;
        }

        public List<PictureBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PictureBean> picture) {
            this.picture = picture;
        }

        public static class PictureBean {
            /**
             * id : 2
             * user_picture_id : 26
             * photo : http://chitchat.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png
             */

            private int id;
            private String user_picture_id;
            private String photo;
            private boolean isChoosed;

            public boolean isChoosed() {
                return isChoosed;
            }

            public void setChoosed(boolean choosed) {
                isChoosed = choosed;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUser_picture_id() {
                return user_picture_id;
            }

            public void setUser_picture_id(String user_picture_id) {
                this.user_picture_id = user_picture_id;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }
}
