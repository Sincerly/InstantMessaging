package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/20 0020
 */
public class UserBlackListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"id":15,"uid":"21","black_uid":"9","nickname":"李四","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"},{"id":14,"uid":"21","black_uid":"8","nickname":"张三","icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png"}]
     */

    private int code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 15
         * uid : 21
         * black_uid : 9
         * nickname : 李四
         * icon : http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
         */

        private int id;
        private String uid;
        private String black_uid;
        private String nickname;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBlack_uid() {
            return black_uid;
        }

        public void setBlack_uid(String black_uid) {
            this.black_uid = black_uid;
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
