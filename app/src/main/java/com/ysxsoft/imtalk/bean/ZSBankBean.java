package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class ZSBankBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"bankList":[{"bank_id":17,"bank_name":"中国银行（1234）"}],"rs":{"uid":"54","type":2,"diamond":"0.00","blv":"1.00","money":0}}
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
         * bankList : [{"bank_id":17,"bank_name":"中国银行（1234）"}]
         * rs : {"uid":"54","type":2,"diamond":"0.00","blv":"1.00","money":0}
         */

        private RsBean rs;
        private List<BankListBean> bankList;

        public RsBean getRs() {
            return rs;
        }

        public void setRs(RsBean rs) {
            this.rs = rs;
        }

        public List<BankListBean> getBankList() {
            return bankList;
        }

        public void setBankList(List<BankListBean> bankList) {
            this.bankList = bankList;
        }

        public static class RsBean {
            /**
             * uid : 54
             * type : 2
             * diamond : 0.00
             * blv : 1.00
             * money : 0
             */

            private String uid;
            private int type;
            private String diamond;
            private String blv;
            private String money;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDiamond() {
                return diamond;
            }

            public void setDiamond(String diamond) {
                this.diamond = diamond;
            }

            public String getBlv() {
                return blv;
            }

            public void setBlv(String blv) {
                this.blv = blv;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }
        }

        public static class BankListBean {
            /**
             * bank_id : 17
             * bank_name : 中国银行（1234）
             */

            private int bank_id;
            private String bank_name;

            public int getBank_id() {
                return bank_id;
            }

            public void setBank_id(int bank_id) {
                this.bank_id = bank_id;
            }

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }
        }
    }
}
