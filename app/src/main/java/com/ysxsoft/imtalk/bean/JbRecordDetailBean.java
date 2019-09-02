package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class JbRecordDetailBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"moneys":"6273.00","recordList":[{"id":374,"uid":"21","type":"2","record_type":7,"status":null,"title":"玫瑰花    收礼人：测试账号1","money":"-4.00","symbol":1,"add_time":"2019-07-31 11:56:46"}]}
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
         * moneys : 6273.00
         * recordList : [{"id":374,"uid":"21","type":"2","record_type":7,"status":null,"title":"玫瑰花    收礼人：测试账号1","money":"-4.00","symbol":1,"add_time":"2019-07-31 11:56:46"}]
         */

        private String moneys;
        private List<RecordListBean> recordList;

        public String getMoneys() {
            return moneys;
        }

        public void setMoneys(String moneys) {
            this.moneys = moneys;
        }

        public List<RecordListBean> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<RecordListBean> recordList) {
            this.recordList = recordList;
        }

        public static class RecordListBean {
            /**
             * id : 374
             * uid : 21
             * type : 2
             * record_type : 7
             * status : null
             * title : 玫瑰花    收礼人：测试账号1
             * money : -4.00
             * symbol : 1
             * add_time : 2019-07-31 11:56:46
             */

            private int id;
            private String uid;
            private String type;
            private String record_type;
            private Object status;
            private String title;
            private String money;
            private int symbol;
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

            public String getRecord_type() {
                return record_type;
            }

            public void setRecord_type(String record_type) {
                this.record_type = record_type;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
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

            public int getSymbol() {
                return symbol;
            }

            public void setSymbol(int symbol) {
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
