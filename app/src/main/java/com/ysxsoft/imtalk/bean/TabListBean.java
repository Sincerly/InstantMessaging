package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class TabListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":28,"label_name":"相亲"},{"id":26,"label_name":"女神"},{"id":25,"label_name":"男神"},{"id":24,"label_name":"娱乐"},{"id":23,"label_name":"游戏"},{"id":22,"label_name":"音乐"}]
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
         * id : 28
         * label_name : 相亲
         */

        private int id;
        private String label_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }
    }
}
