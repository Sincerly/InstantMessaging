package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class SysDetialBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"sid":32,"uid":"21","sys_title":"系统消息","content":"<p style=\"text-align: center;\">系统消息今天有暴雨哦~<\/p><p style=\"text-align: center;\">系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~<img src=\"/uploads/images/20190814/78985f50f660b480f4653c0ca88036a2.png\" title=\"背景.png\" alt=\"\"/><\/p>","sys_desc":"系统消息今天有暴雨哦~~","add_time":"2019-08-14 15:52"}
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
         * sid : 32
         * uid : 21
         * sys_title : 系统消息
         * content : <p style="text-align: center;">系统消息今天有暴雨哦~</p><p style="text-align: center;">系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~系统消息今天有暴雨哦~<img src="/uploads/images/20190814/78985f50f660b480f4653c0ca88036a2.png" title="背景.png" alt=""/></p>
         * sys_desc : 系统消息今天有暴雨哦~~
         * add_time : 2019-08-14 15:52
         */

        private int sid;
        private String uid;
        private String sys_title;
        private String content;
        private String sys_desc;
        private String add_time;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
    }
}
