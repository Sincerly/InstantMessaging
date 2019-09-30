package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/27 0027
 */
public class VersionBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : {"id":1,"vercode":"2","version":"1.0.1","path":"http://chitchat.rhhhyy.com/apks/1.0.1release.apk","content":"版本更新","type":1,"add_time":"2019-09-30"}
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
         * id : 1
         * vercode : 2
         * version : 1.0.1
         * path : http://chitchat.rhhhyy.com/apks/1.0.1release.apk
         * content : 版本更新
         * type : 1
         * add_time : 2019-09-30
         */

        private int id;
        private String vercode;
        private String version;
        private String path;
        private String content;
        private int type;
        private String add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVercode() {
            return vercode;
        }

        public void setVercode(String vercode) {
            this.vercode = vercode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
