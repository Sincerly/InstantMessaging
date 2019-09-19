package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/27 0027
 */
public class AwardRecodeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":64,"aw_name":"独角兽","aw_images":"https://chitchat.sanzhima.cn/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png","aw_gold":1,"add_time":"2019年07月27日 15:32:09"}]
     * last_page : 7
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
         * id : 64
         * aw_name : 独角兽
         * aw_images : https://chitchat.sanzhima.cn/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
         * aw_gold : 1
         * add_time : 2019年07月27日 15:32:09
         */

        private int id;
        private String aw_name;
        private String aw_images;
        private String aw_gold;
        private String add_time;
        private String nums;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNums() {
            return nums == null ? "" : nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
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

        public String getAw_gold() {
            return aw_gold;
        }

        public void setAw_gold(String aw_gold) {
            this.aw_gold = aw_gold;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
