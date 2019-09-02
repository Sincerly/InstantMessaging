package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/9 0009
 */
public class AwardBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"jzdesc":"<p>家族说明<\/p>","cjwt":"<p>家族常见问题说明<\/p>"}
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
         * jzdesc : <p>家族说明</p>
         * cjwt : <p>家族常见问题说明</p>
         */

        private String jzdesc;
        private String cjwt;

        public String getJzdesc() {
            return jzdesc;
        }

        public void setJzdesc(String jzdesc) {
            this.jzdesc = jzdesc;
        }

        public String getCjwt() {
            return cjwt;
        }

        public void setCjwt(String cjwt) {
            this.cjwt = cjwt;
        }
    }
}
