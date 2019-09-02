package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class HomeCateListBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"pids":1,"pname":"首页"}]
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
         * pids : 1
         * pname : 首页
         */

        private String pids;
        private String pname;

        public String getPids() {
            return pids;
        }

        public void setPids(String pids) {
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
