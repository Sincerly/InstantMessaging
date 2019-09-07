package com.ysxsoft.imtalk.bean;

public class EggBean extends CommonBean {


    /**
     * data : {"rid":35,"sg_name":"粉红火箭","aw_pic":"http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png","aw_gif":"http://chitchat.rhhhyy.com/uploads/images/20190826/1b8e43cd206cbbf8ef9c04cbd2cfc3dd.gif"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rid : 35
         * sg_name : 粉红火箭
         * aw_pic : http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png
         * aw_gif : http://chitchat.rhhhyy.com/uploads/images/20190826/1b8e43cd206cbbf8ef9c04cbd2cfc3dd.gif
         */

        private int rid;
        private String sg_name;
        private String aw_pic;
        private String aw_gif;

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getSg_name() {
            return sg_name;
        }

        public void setSg_name(String sg_name) {
            this.sg_name = sg_name;
        }

        public String getAw_pic() {
            return aw_pic;
        }

        public void setAw_pic(String aw_pic) {
            this.aw_pic = aw_pic;
        }

        public String getAw_gif() {
            return aw_gif;
        }

        public void setAw_gif(String aw_gif) {
            this.aw_gif = aw_gif;
        }
    }
}
