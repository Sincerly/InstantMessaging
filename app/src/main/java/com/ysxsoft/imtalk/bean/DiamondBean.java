package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class DiamondBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"uid":"21","now_money":"6273.00","dh_desc":"兑换比率：1金币 = 0.10钻石","thbl":"0.10","diamonds":627.3}
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
         * uid : 21
         * now_money : 6273.00
         * dh_desc : 兑换比率：1金币 = 0.10钻石
         * thbl : 0.10
         * diamonds : 627.3
         */

        private String uid;
        private String now_money;
        private String dh_desc;
        private String thbl;
        private String diamonds;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNow_money() {
            return now_money;
        }

        public void setNow_money(String now_money) {
            this.now_money = now_money;
        }

        public String getDh_desc() {
            return dh_desc;
        }

        public void setDh_desc(String dh_desc) {
            this.dh_desc = dh_desc;
        }

        public String getThbl() {
            return thbl;
        }

        public void setThbl(String thbl) {
            this.thbl = thbl;
        }

        public String getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(String diamonds) {
            this.diamonds = diamonds;
        }
    }
}
