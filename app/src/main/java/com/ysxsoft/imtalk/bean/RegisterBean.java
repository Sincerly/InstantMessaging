package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class RegisterBean {

    /**
     * code : 0
     * msg : 注册成功
     * data : {"uid":"20","token":"I4+XhLDlUSVS88MYF7qd9uOl9YBJNT+ozWTqIy2ZJPyQFxVLiWDLNesV4nZ93JTfN0G5yqlob3/TlyMU2yVRng=="}
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
         * uid : 20
         * token : I4+XhLDlUSVS88MYF7qd9uOl9YBJNT+ozWTqIy2ZJPyQFxVLiWDLNesV4nZ93JTfN0G5yqlob3/TlyMU2yVRng==
         */

        private String uid;
        private String token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
