package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class UserLevelBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"sign_rule":"<p style=\"text-align: center;\"><img src=\"/uploads/images/20190717/5709849cfe7a3885413faf2e6fa99bf1.png\" title=\"image.png\" alt=\"\"/><\/p>","icon":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","user_level_name":"Lv.","now_level":1,"next_level_gold":10}
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
         * sign_rule : <p style="text-align: center;"><img src="/uploads/images/20190717/5709849cfe7a3885413faf2e6fa99bf1.png" title="image.png" alt=""/></p>
         * icon : http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
         * user_level_name : Lv.
         * now_level : 1
         * next_level_gold : 10
         */

        private String sign_rule;
        private String icon;
        private String user_level_name;
        private String now_level;
        private String next_level_gold;

        public String getSign_rule() {
            return sign_rule;
        }

        public void setSign_rule(String sign_rule) {
            this.sign_rule = sign_rule;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUser_level_name() {
            return user_level_name;
        }

        public void setUser_level_name(String user_level_name) {
            this.user_level_name = user_level_name;
        }

        public String getNow_level() {
            return now_level;
        }

        public void setNow_level(String now_level) {
            this.now_level = now_level;
        }

        public String getNext_level_gold() {
            return next_level_gold;
        }

        public void setNext_level_gold(String next_level_gold) {
            this.next_level_gold = next_level_gold;
        }
    }
}
