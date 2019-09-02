package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class BannerBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":37,"pic":"http://chitchat.com/uploads/images/20190703/ffd664a3495749154e6c8642fdd26688.png","type":3}]
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
         * id : 37
         * pic : http://chitchat.com/uploads/images/20190703/ffd664a3495749154e6c8642fdd26688.png
         * type : 3
         */

        private int id;
        private String pic;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
