package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/9/21 0021
 */
public class GroupIdBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"groupId":12,"is_real":1}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * groupId : 12
         * is_real : 1
         */

        private String groupId;
        private int is_real;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public int getIs_real() {
            return is_real;
        }

        public void setIs_real(int is_real) {
            this.is_real = is_real;
        }
    }
}
