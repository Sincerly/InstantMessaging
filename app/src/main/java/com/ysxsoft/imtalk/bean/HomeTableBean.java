package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class HomeTableBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"pids":4,"pname":"推荐"},{"pids":7,"pname":"女神"},{"pids":10,"pname":"男神"}]
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
         * pids : 4
         * pname : 推荐
         */

        private int pids;
        private String pname;

        public int getPids() {
            return pids;
        }

        public void setPids(int pids) {
            this.pids = pids;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }
    }
}
