package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class SGiftBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : {"listInfo":[{"id":2295,"name":"灰机","gold":"100.00","days":"已过期","is_dated":1,"is_use":0,"pic":"http://chitchat.com/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png"}],"sum":8}
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
         * listInfo : [{"id":2295,"name":"灰机","gold":"100.00","days":"已过期","is_dated":1,"is_use":0,"pic":"http://chitchat.com/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png"}]
         * sum : 8
         */

        private int sum;
        private List<ListInfoBean> listInfo;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public List<ListInfoBean> getListInfo() {
            return listInfo;
        }

        public void setListInfo(List<ListInfoBean> listInfo) {
            this.listInfo = listInfo;
        }

        public static class ListInfoBean {
            /**
             * id : 2295
             * name : 灰机
             * gold : 100.00
             * days : 已过期
             * is_dated : 1
             * is_use : 0
             * pic : http://chitchat.com/uploads/images/20190708/7ab5795f106ad36d96096d241edb6244.png
             */

            private int id;
            private String name;
            private String gold;
            private String days;
            private int is_dated;
            private int is_use;
            private String pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getGold() {
                return gold;
            }

            public void setGold(String gold) {
                this.gold = gold;
            }

            public String getDays() {
                return days;
            }

            public void setDays(String days) {
                this.days = days;
            }

            public int getIs_dated() {
                return is_dated;
            }

            public void setIs_dated(int is_dated) {
                this.is_dated = is_dated;
            }

            public int getIs_use() {
                return is_use;
            }

            public void setIs_use(int is_use) {
                this.is_use = is_use;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
