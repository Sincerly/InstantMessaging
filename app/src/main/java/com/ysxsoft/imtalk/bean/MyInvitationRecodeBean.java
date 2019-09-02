package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/26 0026
 */
public class MyInvitationRecodeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"user_info":[{"uid":8,"icon":"36","username":"张","user_gold":10},{"uid":9,"icon":"36","username":"王","user_gold":20}],"total_gold":30,"total_num":2}
     * last_page : 1
     */

    private int code;
    private String msg;
    private DataBean data;
    private int last_page;

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

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public static class DataBean {
        /**
         * user_info : [{"uid":8,"icon":"36","username":"张","user_gold":10},{"uid":9,"icon":"36","username":"王","user_gold":20}]
         * total_gold : 30
         * total_num : 2
         */

        private String total_gold;
        private String total_num;
        private List<UserInfoBean> user_info;

        public String getTotal_gold() {
            return total_gold;
        }

        public void setTotal_gold(String total_gold) {
            this.total_gold = total_gold;
        }

        public String getTotal_num() {
            return total_num;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
        }

        public List<UserInfoBean> getUser_info() {
            return user_info;
        }

        public void setUser_info(List<UserInfoBean> user_info) {
            this.user_info = user_info;
        }

        public static class UserInfoBean {
            /**
             * uid : 8
             * icon : 36
             * username : 张
             * user_gold : 10
             */

            private int uid;
            private String icon;
            private String username;
            private String user_gold;

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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUser_gold() {
                return user_gold;
            }

            public void setUser_gold(String user_gold) {
                this.user_gold = user_gold;
            }
        }
    }
}
