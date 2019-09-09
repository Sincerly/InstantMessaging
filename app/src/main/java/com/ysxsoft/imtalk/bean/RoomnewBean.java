package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/19 0019
 */
public class RoomnewBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"result":{"roomInfo":{"uid":24,"room_id":"686","room_name":"黑芝麻胡","add_time":"2019-09-02 10:06:12","room_num":"0192471","room_notice":"房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告","room_label":"聊天","room_desc":"在县城V字形从v","room_content":"是大法师的发","is_lock":null,"lock_pwd":null,"room_bg":"http://chitchat.rhhhyy.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png","room_gift_tx":1,"room_is_fair":1,"room_pure":0,"memCount":2,"is_wheat":0,"nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","gifts":80385,"is_lock_wheat":1,"is_oc_wheat":0},"roomUserList":[{"uid":"24","nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","ml_level":10,"user_level":20,"sort":0,"role":1,"is_wheat":0},{"uid":"23","nickname":"我是小嗨嗨6126","sex":1,"icon":"http://chitchat.rhhhyy.com/static/admin/img/icon.png","ml_level":10,"user_level":20,"sort":100,"role":3,"is_wheat":0}],"userCount":2,"adminListInfo":[],"micPositions":[{"mw_id":6217,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":1,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6218,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":2,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6219,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":3,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6220,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":4,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6221,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":5,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6222,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":6,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6223,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":7,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6224,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":8,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0}]}}
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
         * result : {"roomInfo":{"uid":24,"room_id":"686","room_name":"黑芝麻胡","add_time":"2019-09-02 10:06:12","room_num":"0192471","room_notice":"房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告","room_label":"聊天","room_desc":"在县城V字形从v","room_content":"是大法师的发","is_lock":null,"lock_pwd":null,"room_bg":"http://chitchat.rhhhyy.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png","room_gift_tx":1,"room_is_fair":1,"room_pure":0,"memCount":2,"is_wheat":0,"nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","gifts":80385,"is_lock_wheat":1,"is_oc_wheat":0},"roomUserList":[{"uid":"24","nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","ml_level":10,"user_level":20,"sort":0,"role":1,"is_wheat":0},{"uid":"23","nickname":"我是小嗨嗨6126","sex":1,"icon":"http://chitchat.rhhhyy.com/static/admin/img/icon.png","ml_level":10,"user_level":20,"sort":100,"role":3,"is_wheat":0}],"userCount":2,"adminListInfo":[],"micPositions":[{"mw_id":6217,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":1,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6218,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":2,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6219,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":3,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6220,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":4,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6221,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":5,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6222,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":6,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6223,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":7,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6224,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":8,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * roomInfo : {"uid":24,"room_id":"686","room_name":"黑芝麻胡","add_time":"2019-09-02 10:06:12","room_num":"0192471","room_notice":"房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告","room_label":"聊天","room_desc":"在县城V字形从v","room_content":"是大法师的发","is_lock":null,"lock_pwd":null,"room_bg":"http://chitchat.rhhhyy.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png","room_gift_tx":1,"room_is_fair":1,"room_pure":0,"memCount":2,"is_wheat":0,"nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","gifts":80385,"is_lock_wheat":1,"is_oc_wheat":0}
             * roomUserList : [{"uid":"24","nickname":"黑芝麻胡","sex":1,"icon":"http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg","ml_level":10,"user_level":20,"sort":0,"role":1,"is_wheat":0},{"uid":"23","nickname":"我是小嗨嗨6126","sex":1,"icon":"http://chitchat.rhhhyy.com/static/admin/img/icon.png","ml_level":10,"user_level":20,"sort":100,"role":3,"is_wheat":0}]
             * userCount : 2
             * adminListInfo : []
             * micPositions : [{"mw_id":6217,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":1,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6218,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":2,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6219,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":3,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6220,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":4,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6221,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":5,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6222,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":6,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6223,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":7,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0},{"mw_id":6224,"room_id":686,"uid":0,"icon":"http://chitchat.rhhhyy.com/static/admin/img/none.png","sex":null,"sort":8,"nickname":"号麦位","is_room":0,"is_admin":0,"is_block":0,"is_wheat":0,"is_lock_wheat":1,"is_leave":0,"is_oc_wheat":0,"add_time":"1970-01-01 08:00:00","gifts":0}]
             */

            private RoomInfoBean roomInfo;
            private int userCount;
            private List<RoomUserListBean> roomUserList;
            private List<?> adminListInfo;
            private List<MicPositionsBean> micPositions;

            public RoomInfoBean getRoomInfo() {
                return roomInfo;
            }

            public void setRoomInfo(RoomInfoBean roomInfo) {
                this.roomInfo = roomInfo;
            }

            public int getUserCount() {
                return userCount;
            }

            public void setUserCount(int userCount) {
                this.userCount = userCount;
            }

            public List<RoomUserListBean> getRoomUserList() {
                return roomUserList;
            }

            public void setRoomUserList(List<RoomUserListBean> roomUserList) {
                this.roomUserList = roomUserList;
            }

            public List<?> getAdminListInfo() {
                return adminListInfo;
            }

            public void setAdminListInfo(List<?> adminListInfo) {
                this.adminListInfo = adminListInfo;
            }

            public List<MicPositionsBean> getMicPositions() {
                return micPositions;
            }

            public void setMicPositions(List<MicPositionsBean> micPositions) {
                this.micPositions = micPositions;
            }

            public static class RoomInfoBean {
                /**
                 * uid : 24
                 * room_id : 686
                 * room_name : 黑芝麻胡
                 * add_time : 2019-09-02 10:06:12
                 * room_num : 0192471
                 * room_notice : 房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告房间系统公告
                 * room_label : 聊天
                 * room_desc : 在县城V字形从v
                 * room_content : 是大法师的发
                 * is_lock : null
                 * lock_pwd : null
                 * room_bg : http://chitchat.rhhhyy.com/uploads/images/20190708/079ee760c926ad9aac7723534f2759eb.png
                 * room_gift_tx : 1
                 * room_is_fair : 1
                 * room_pure : 0
                 * memCount : 2
                 * is_wheat : 0
                 * nickname : 黑芝麻胡
                 * sex : 1
                 * icon : http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg
                 * gifts : 80385
                 * is_lock_wheat : 1
                 * is_oc_wheat : 0
                 */

                private int uid;
                private String room_id;
                private String room_name;
                private String add_time;
                private String room_num;
                private String room_notice;
                private String room_label;
                private String room_desc;
                private String room_content;
                private Object is_lock;
                private Object lock_pwd;
                private String room_bg;
                private int room_gift_tx;
                private int room_is_fair;
                private int room_pure;
                private int memCount;
                private int is_wheat;
                private String nickname;
                private int sex;
                private String icon;
                private int gifts;
                private int is_lock_wheat;
                private int is_oc_wheat;

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
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

                public Object getIs_lock() {
                    return is_lock;
                }

                public void setIs_lock(Object is_lock) {
                    this.is_lock = is_lock;
                }

                public Object getLock_pwd() {
                    return lock_pwd;
                }

                public void setLock_pwd(Object lock_pwd) {
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

                public int getMemCount() {
                    return memCount;
                }

                public void setMemCount(int memCount) {
                    this.memCount = memCount;
                }

                public int getIs_wheat() {
                    return is_wheat;
                }

                public void setIs_wheat(int is_wheat) {
                    this.is_wheat = is_wheat;
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

                public int getGifts() {
                    return gifts;
                }

                public void setGifts(int gifts) {
                    this.gifts = gifts;
                }

                public int getIs_lock_wheat() {
                    return is_lock_wheat;
                }

                public void setIs_lock_wheat(int is_lock_wheat) {
                    this.is_lock_wheat = is_lock_wheat;
                }

                public int getIs_oc_wheat() {
                    return is_oc_wheat;
                }

                public void setIs_oc_wheat(int is_oc_wheat) {
                    this.is_oc_wheat = is_oc_wheat;
                }
            }

            public static class RoomUserListBean {
                /**
                 * uid : 24
                 * nickname : 黑芝麻胡
                 * sex : 1
                 * icon : http://chitchat.rhhhyy.com/uploads/images/20190830/crop_20190823100954_3409016.jpg
                 * ml_level : 10
                 * user_level : 20
                 * sort : 0
                 * role : 1
                 * is_wheat : 0
                 */

                private String uid;
                private String nickname;
                private int sex;
                private String icon;
                private int ml_level;
                private int user_level;
                private int sort;
                private int role;
                private int is_wheat;

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

                public int getMl_level() {
                    return ml_level;
                }

                public void setMl_level(int ml_level) {
                    this.ml_level = ml_level;
                }

                public int getUser_level() {
                    return user_level;
                }

                public void setUser_level(int user_level) {
                    this.user_level = user_level;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getRole() {
                    return role;
                }

                public void setRole(int role) {
                    this.role = role;
                }

                public int getIs_wheat() {
                    return is_wheat;
                }

                public void setIs_wheat(int is_wheat) {
                    this.is_wheat = is_wheat;
                }
            }

            public static class MicPositionsBean {
                /**
                 * mw_id : 6217
                 * room_id : 686
                 * uid : 0
                 * icon : http://chitchat.rhhhyy.com/static/admin/img/none.png
                 * sex : null
                 * sort : 1
                 * nickname : 号麦位
                 * is_room : 0
                 * is_admin : 0
                 * is_block : 0
                 * is_wheat : 0
                 * is_lock_wheat : 1
                 * is_leave : 0
                 * is_oc_wheat : 0
                 * add_time : 1970-01-01 08:00:00
                 * gifts : 0
                 */

                private int mw_id;
                private int room_id;
                private int uid;
                private String icon;
                private Object sex;
                private int sort;
                private String nickname;
                private int is_room;
                private int is_admin;
                private int is_block;
                private int is_wheat;
                private int is_lock_wheat;
                private int is_leave;
                private int is_oc_wheat;
                private String add_time;
                private int gifts;

                public int getMw_id() {
                    return mw_id;
                }

                public void setMw_id(int mw_id) {
                    this.mw_id = mw_id;
                }

                public int getRoom_id() {
                    return room_id;
                }

                public void setRoom_id(int room_id) {
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

                public Object getSex() {
                    return sex;
                }

                public void setSex(Object sex) {
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

                public int getIs_leave() {
                    return is_leave;
                }

                public void setIs_leave(int is_leave) {
                    this.is_leave = is_leave;
                }

                public int getIs_oc_wheat() {
                    return is_oc_wheat;
                }

                public void setIs_oc_wheat(int is_oc_wheat) {
                    this.is_oc_wheat = is_oc_wheat;
                }

                public String getAdd_time() {
                    return add_time;
                }

                public void setAdd_time(String add_time) {
                    this.add_time = add_time;
                }

                public int getGifts() {
                    return gifts;
                }

                public void setGifts(int gifts) {
                    this.gifts = gifts;
                }
            }
        }
    }
}
