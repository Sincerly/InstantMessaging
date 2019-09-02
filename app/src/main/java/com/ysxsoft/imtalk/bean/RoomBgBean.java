package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/30 0030
 */
public class RoomBgBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":6,"bg_name":"背景图22","bg_url":"http://chitchat.com/uploads/images/20190722/4802e100f661e197bbbd06924434ad5f.png"},{"id":5,"bg_name":"背景图","bg_url":"http://chitchat.com/uploads/images/20190722/16667afd8fb11c6f4295bb923ac0abb9.png"}]
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
         * id : 6
         * bg_name : 背景图22
         * bg_url : http://chitchat.com/uploads/images/20190722/4802e100f661e197bbbd06924434ad5f.png
         */

        private String id;
        private String bg_name;
        private String bg_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBg_name() {
            return bg_name;
        }

        public void setBg_name(String bg_name) {
            this.bg_name = bg_name;
        }

        public String getBg_url() {
            return bg_url;
        }

        public void setBg_url(String bg_url) {
            this.bg_url = bg_url;
        }
    }
}
