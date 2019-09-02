package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/26 0026
 */
public class MyQrCodeBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"invitation_code":"078884","qr_code":"http://chitchat.com/uploads/qrcode/20190704180840_2.png","total_gold":30,"total_num":2,"share_url":"http://chitchat.com/#/register/register?uid=2"}
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
         * invitation_code : 078884
         * qr_code : http://chitchat.com/uploads/qrcode/20190704180840_2.png
         * total_gold : 30
         * total_num : 2
         * share_url : http://chitchat.com/#/register/register?uid=2
         */

        private String invitation_code;
        private String qr_code;
        private int total_gold;
        private int total_num;
        private String share_url;

        public String getInvitation_code() {
            return invitation_code;
        }

        public void setInvitation_code(String invitation_code) {
            this.invitation_code = invitation_code;
        }

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public int getTotal_gold() {
            return total_gold;
        }

        public void setTotal_gold(int total_gold) {
            this.total_gold = total_gold;
        }

        public int getTotal_num() {
            return total_num;
        }

        public void setTotal_num(int total_num) {
            this.total_num = total_num;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}
