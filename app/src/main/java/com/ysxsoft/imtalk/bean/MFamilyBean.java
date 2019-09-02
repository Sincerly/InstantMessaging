package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/9 0009
 */
public class MFamilyBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"fmy_sn":"4153","fmy_name":"吃货家族","fmy_pic":"http://chitchat.sanzhima.cn/uploads/images/20190724/1548406553724_8778910.jpg","fmy_num":3,"is_fmy":2}
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
         * fmy_sn : 4153
         * fmy_name : 吃货家族
         * fmy_pic : http://chitchat.sanzhima.cn/uploads/images/20190724/1548406553724_8778910.jpg
         * fmy_num : 3
         * is_fmy : 2
         */

        private String fmy_sn;
        private String fmy_name;
        private String fmy_pic;
        private String fmy_num;
        private String is_fmy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getFmy_sn() {
            return fmy_sn;
        }

        public void setFmy_sn(String fmy_sn) {
            this.fmy_sn = fmy_sn;
        }

        public String getFmy_name() {
            return fmy_name;
        }

        public void setFmy_name(String fmy_name) {
            this.fmy_name = fmy_name;
        }

        public String getFmy_pic() {
            return fmy_pic;
        }

        public void setFmy_pic(String fmy_pic) {
            this.fmy_pic = fmy_pic;
        }

        public String getFmy_num() {
            return fmy_num;
        }

        public void setFmy_num(String fmy_num) {
            this.fmy_num = fmy_num;
        }

        public String getIs_fmy() {
            return is_fmy;
        }

        public void setIs_fmy(String is_fmy) {
            this.is_fmy = is_fmy;
        }
    }
}
