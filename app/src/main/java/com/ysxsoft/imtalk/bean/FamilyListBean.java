package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/9 0009
 */
public class FamilyListBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":41,"uid":"21","fmy_name":"吃货家族A","fmy_pic":"http://chitchat.com/uploads/images/20190727/d4fa1392ee6a0de2cc87e327d1ec7c1e.png","fmy_num":1,"is_fmy":1},{"id":40,"uid":"21","fmy_name":"吃货家族","fmy_pic":"http://chitchat.com/uploads/images/20190727/d4fa1392ee6a0de2cc87e327d1ec7c1e.png","fmy_num":2,"is_fmy":3}]
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
         * id : 41
         * uid : 21
         * fmy_name : 吃货家族A
         * fmy_pic : http://chitchat.com/uploads/images/20190727/d4fa1392ee6a0de2cc87e327d1ec7c1e.png
         * fmy_num : 1
         * is_fmy : 1
         */

        private int id;
        private String uid;
        private String fmy_name;
        private String fmy_pic;
        private String fmy_num;
        private String is_fmy;

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
