package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class BannerDetailBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"id":39,"content":"<p>fdsa<\/p>"}
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
         * id : 39
         * content : <p>fdsa</p>
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
