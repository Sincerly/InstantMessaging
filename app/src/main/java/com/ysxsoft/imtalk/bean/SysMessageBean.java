package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/23 0023
 */
public class SysMessageBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"userInfo":[{"id":1,"is_service":1,"nickname":"客服","icon":"http://chitchat.com/uploads/images/20190821/9b7179338557ef54a9dd1e6bbaca3c05.png"},{"id":3,"is_service":2,"nickname":"系统消息","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}],"sys":{"sys_status":1,"total_num":2}}
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
         * userInfo : [{"id":1,"is_service":1,"nickname":"客服","icon":"http://chitchat.com/uploads/images/20190821/9b7179338557ef54a9dd1e6bbaca3c05.png"},{"id":3,"is_service":2,"nickname":"系统消息","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}]
         * sys : {"sys_status":1,"total_num":2}
         */

        private SysBean sys;
        private List<UserInfoBean> userInfo;

        public SysBean getSys() {
            return sys;
        }

        public void setSys(SysBean sys) {
            this.sys = sys;
        }

        public List<UserInfoBean> getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(List<UserInfoBean> userInfo) {
            this.userInfo = userInfo;
        }

        public static class SysBean {
            /**
             * sys_status : 1
             * total_num : 2
             */

            private int sys_status;
            private String total_num;

            public int getSys_status() {
                return sys_status;
            }

            public void setSys_status(int sys_status) {
                this.sys_status = sys_status;
            }

            public String getTotal_num() {
                return total_num;
            }

            public void setTotal_num(String total_num) {
                this.total_num = total_num;
            }
        }

        public static class UserInfoBean {
            /**
             * id : 1
             * is_service : 1
             * nickname : 客服
             * icon : http://chitchat.com/uploads/images/20190821/9b7179338557ef54a9dd1e6bbaca3c05.png
             */

            private int id;
            private int is_service;
            private String nickname;
            private String icon;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_service() {
                return is_service;
            }

            public void setIs_service(int is_service) {
                this.is_service = is_service;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
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
