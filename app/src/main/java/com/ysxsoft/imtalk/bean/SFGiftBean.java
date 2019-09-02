package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class SFGiftBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"listInfo":[{"id":2283,"name":"独角兽","gold":"1.00","award_num":"4","pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}],"sum":12,"total_gold":12}
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
         * listInfo : [{"id":2283,"name":"独角兽","gold":"1.00","award_num":"4","pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]
         * sum : 12
         * total_gold : 12
         */

        private String sum;
        private int total_gold;
        private List<ListInfoBean> listInfo;

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public int getTotal_gold() {
            return total_gold;
        }

        public void setTotal_gold(int total_gold) {
            this.total_gold = total_gold;
        }

        public List<ListInfoBean> getListInfo() {
            return listInfo;
        }

        public void setListInfo(List<ListInfoBean> listInfo) {
            this.listInfo = listInfo;
        }

        public static class ListInfoBean {
            /**
             * id : 2283
             * name : 独角兽
             * gold : 1.00
             * award_num : 4
             * pic : http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
             */

            private int id;
            private String name;
            private String gold;
            private String award_num;
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

            public String getAward_num() {
                return award_num;
            }

            public void setAward_num(String award_num) {
                this.award_num = award_num;
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
