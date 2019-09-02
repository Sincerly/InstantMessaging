package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/23 0023
 */
public class ContactBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"desc":"官方QQ群1：123456789\r\n官方QQ群2：123456789\r\n官方QQ群3：123456789\r\n合作邮箱：liaotian520@qq.com","content":"客服电话：123456789\r\n微信客服：123456789"}
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
         * desc : 官方QQ群1：123456789
         官方QQ群2：123456789
         官方QQ群3：123456789
         合作邮箱：liaotian520@qq.com
         * content : 客服电话：123456789
         微信客服：123456789
         */

        private String desc;
        private String content;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
