package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class SysBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"sid":32,"uid":"21","sys_title":"系统消息","sys_desc":"系统消息今天有暴雨哦~~","add_time":"2019-08-14 15:52","sys_status":2}]
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
         * sid : 32
         * uid : 21
         * sys_title : 系统消息
         * sys_desc : 系统消息今天有暴雨哦~~
         * add_time : 2019-08-14 15:52
         * sys_status : 2
         */

        private int sid;
        private String uid;
        private String sys_title;
        private String sys_desc;
        private String add_time;
        private int sys_status;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSys_title() {
            return sys_title;
        }

        public void setSys_title(String sys_title) {
            this.sys_title = sys_title;
        }

        public String getSys_desc() {
            return sys_desc;
        }

        public void setSys_desc(String sys_desc) {
            this.sys_desc = sys_desc;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getSys_status() {
            return sys_status;
        }

        public void setSys_status(int sys_status) {
            this.sys_status = sys_status;
        }
    }
}
