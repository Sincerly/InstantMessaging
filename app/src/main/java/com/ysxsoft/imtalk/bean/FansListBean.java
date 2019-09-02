package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/6 0006
 */
public class FansListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"id":9,"uid":20,"fs_id":"21","nickname":"Amor_LL","icon":"http://chitchat.com/static/admin/img/icon.png","sex":null,"user_desc":null,"ml_level":1,"th_level":1,"status":1},{"id":8,"uid":12,"fs_id":"21","nickname":"测试账号1","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","sex":null,"user_desc":"123456","ml_level":10,"th_level":1,"status":2}]
     * last_page : 1
     */

    private int code;
    private String msg;
    private int last_page;
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

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 9
         * uid : 20
         * fs_id : 21
         * nickname : Amor_LL
         * icon : http://chitchat.com/static/admin/img/icon.png
         * sex : null
         * user_desc : null
         * ml_level : 1
         * th_level : 1
         * status : 1
         */

        private int id;
        private int uid;
        private String fs_id;
        private String nickname;
        private String icon;
        private String sex;
        private String user_desc;
        private String ml_level;
        private String th_level;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getFs_id() {
            return fs_id;
        }

        public void setFs_id(String fs_id) {
            this.fs_id = fs_id;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_desc() {
            return user_desc;
        }

        public void setUser_desc(String user_desc) {
            this.user_desc = user_desc;
        }

        public String getMl_level() {
            return ml_level;
        }

        public void setMl_level(String ml_level) {
            this.ml_level = ml_level;
        }

        public String getTh_level() {
            return th_level;
        }

        public void setTh_level(String th_level) {
            this.th_level = th_level;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
