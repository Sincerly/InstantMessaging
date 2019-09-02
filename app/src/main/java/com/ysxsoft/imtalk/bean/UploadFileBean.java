package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class UploadFileBean {


    /**
     * code : 0
     * message : 成功
     * data : {"id":"15","url":"http://aaa.uploads/images/20190322/5c0116d2Nba709b15_1911315.jpg"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 15
         * url : http://aaa.uploads/images/20190322/5c0116d2Nba709b15_1911315.jpg
         */

        private String id;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
