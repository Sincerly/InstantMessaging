package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/12 0012
 */
public class RecordDetailBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"diamond":"72.00","recordList":[{"id":416,"uid":"21","type":"1","record_type":4,"status":null,"title":"钻石兑换金币 - 扣除的钻石","money":"-10.00","symbol":1,"add_time":"2019-08-01 10:31:09"}]}
     * last_page : 1
     */

    private int code;
    private String msg;
    private DataBean data;
    private int last_page;

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

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public static class DataBean {
        /**
         * diamond : 72.00
         * recordList : [{"id":416,"uid":"21","type":"1","record_type":4,"status":null,"title":"钻石兑换金币 - 扣除的钻石","money":"-10.00","symbol":1,"add_time":"2019-08-01 10:31:09"}]
         */

        private String diamond;
        private List<RecordListBean> recordList;

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public List<RecordListBean> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<RecordListBean> recordList) {
            this.recordList = recordList;
        }

        public static class RecordListBean {
            /**
             * id : 416
             * uid : 21
             * type : 1
             * record_type : 4
             * status : null
             * title : 钻石兑换金币 - 扣除的钻石
             * money : -10.00
             * symbol : 1
             * add_time : 2019-08-01 10:31:09
             */

            private int id;
            private String uid;
            private String type;
            private int record_type;
            private String status;
            private String title;
            private String money;
            private String symbol;
            private String add_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getRecord_type() {
                return record_type;
            }

            public void setRecord_type(int record_type) {
                this.record_type = record_type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }
    }
}
