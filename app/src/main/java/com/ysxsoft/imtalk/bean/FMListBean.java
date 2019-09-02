package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/9 0009
 */
public class FMListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"is_fmy":1,"uid":2,"nickname":"Amor_L","icon":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}
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
         * is_fmy : 1
         * uid : 2
         * nickname : Amor_L
         * icon : http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
         */

        private String is_fmy;
        private String uid;
        private String nickname;
        private String icon;

        public String getIs_fmy() {
            return is_fmy;
        }

        public void setIs_fmy(String is_fmy) {
            this.is_fmy = is_fmy;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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
    }
}
