package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/6 0006
 */
public class BageListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":94,"gift_num":40,"aw_name":"独角兽","aw_images":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]
     * last_page : 1
     */

    private String code;
    private String msg;
    private int last_page;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 94
         * gift_num : 40
         * aw_name : 独角兽
         * aw_images : http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
         */

        private int id;
        private String gift_num;
        private String aw_name;
        private String aw_images;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getAw_gif() {
            return aw_gif;
        }

        public void setAw_gif(String aw_gif) {
            this.aw_gif = aw_gif;
        }

        private String aw_gif;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGift_num() {
            return gift_num;
        }

        public void setGift_num(String gift_num) {
            this.gift_num = gift_num;
        }

        public String getAw_name() {
            return aw_name;
        }

        public void setAw_name(String aw_name) {
            this.aw_name = aw_name;
        }

        public String getAw_images() {
            return aw_images;
        }

        public void setAw_images(String aw_images) {
            this.aw_images = aw_images;
        }
    }
}
