package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class DressMallBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":12,"name":"灰机","gold":"100.00","days":5,"pic":"http://chitchat.sanzhima.cn/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png"}]
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
         * id : 12
         * name : 灰机
         * gold : 100.00
         * days : 5
         * pic : http://chitchat.sanzhima.cn/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png
         */

        private int id;
        private String name;
        private String gold;
        private int days;
        private String pic;
        private long size;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getGif_pic() {
            return gif_pic;
        }

        public void setGif_pic(String gif_pic) {
            this.gif_pic = gif_pic;
        }

        private String gif_pic;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        private boolean isSelect;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
