package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/12 0012
 */
public class ShareUserBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"desc":"嗨嗨，与喜欢的声音不期而遇","nickname":"我是小嗨嗨1421带你走进Ta的房间 --- 黑芝麻胡","icon":"http://chitchat.com/static/admin/img/icon.png","url":"http://chitchat.com/#/user/index?vcode=861708"}
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
         * desc : 嗨嗨，与喜欢的声音不期而遇
         * nickname : 我是小嗨嗨1421带你走进Ta的房间 --- 黑芝麻胡
         * icon : http://chitchat.com/static/admin/img/icon.png
         * url : http://chitchat.com/#/user/index?vcode=861708
         */

        private String desc;
        private String nickname;
        private String icon;
        private String url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
