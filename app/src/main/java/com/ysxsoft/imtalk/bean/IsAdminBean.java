package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/17 0017
 */
public class IsAdminBean {


    /**
     * code : 0
     * msg : 成功！
     * data : {"is_admin":0,"is_wheat":"","is_lock_wheat":"","is_oc_wheat":"","sort_id":""}
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
         * is_admin : 0
         * is_wheat :
         * is_lock_wheat :
         * is_oc_wheat :
         * sort_id :
         */

        private int is_admin;
        private String is_wheat;
        private String is_lock_wheat;
        private String is_oc_wheat;
        private String sort_id;
        private String nickname;

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

        private String icon;

        public int getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(int is_admin) {
            this.is_admin = is_admin;
        }

        public String getIs_wheat() {
            return is_wheat;
        }

        public void setIs_wheat(String is_wheat) {
            this.is_wheat = is_wheat;
        }

        public String getIs_lock_wheat() {
            return is_lock_wheat;
        }

        public void setIs_lock_wheat(String is_lock_wheat) {
            this.is_lock_wheat = is_lock_wheat;
        }

        public String getIs_oc_wheat() {
            return is_oc_wheat;
        }

        public void setIs_oc_wheat(String is_oc_wheat) {
            this.is_oc_wheat = is_oc_wheat;
        }

        public String getSort_id() {
            return sort_id;
        }

        public void setSort_id(String sort_id) {
            this.sort_id = sort_id;
        }
    }
}
