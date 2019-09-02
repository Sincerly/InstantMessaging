package com.ysxsoft.imtalk.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Create By 胡
 * on 2019/8/16 0016
 */
public class WxPayBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"prepayid":"wx141109152063051f91a01da21822113000","appid":"wx1093d59a2dd55f91","partnerid":"1545726891","package":"Sign=WXPay","noncestr":"wx_20190814110913133850","timestamp":1565752154,"sign":"BFEF67CB451FCD3F228FB60AF80323BF"}
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
         * prepayid : wx141109152063051f91a01da21822113000
         * appid : wx1093d59a2dd55f91
         * partnerid : 1545726891
         * package : Sign=WXPay
         * noncestr : wx_20190814110913133850
         * timestamp : 1565752154
         * sign : BFEF67CB451FCD3F228FB60AF80323BF
         */

        private String prepayid;
        private String appid;
        private String partnerid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private int timestamp;
        private String sign;

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
