package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class FaceListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":15,"bq_name":"表情1","bq_pic":"http://chitchat.sanzhima.cn/uploads/images/20190708/dfffc0ce49aa0240879b71049b03bc7b.gif"}]
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
         * bq_name : 表情1
         * bq_pic : http://chitchat.sanzhima.cn/uploads/images/20190708/dfffc0ce49aa0240879b71049b03bc7b.gif
         */

        private int id;
        private String bq_name;
        private String bq_pic;
        private String bq_gif;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBq_name() {
            return bq_name;
        }

        public void setBq_name(String bq_name) {
            this.bq_name = bq_name;
        }

        public String getBq_pic() {
            return bq_pic;
        }

        public void setBq_pic(String bq_pic) {
            this.bq_pic = bq_pic;
        }

        public String getBq_gif() {
            return bq_gif == null ? "" : bq_gif;
        }

        public void setBq_gif(String bq_gif) {
            this.bq_gif = bq_gif;
        }
    }
}
