package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/27 0027
 */
public class AwardListDataBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"awardList":[{"id":34,"sg_name":"百合花","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"},{"id":33,"sg_name":"独角兽","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"},{"id":32,"sg_name":"红玫瑰","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}],"act_rules":"<p>砸金蛋规则11<\/p>","zjd_gold_num":"1次消耗20.00金币","zjd_times":[{"id":18,"title":"1次","times":"11.00"},{"id":19,"title":"10次","times":"10.00"},{"id":20,"title":"100次","times":"100.00"},{"id":21,"title":"自动砸","times":"1.00"}],"money":"8000.00"}
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
         * awardList : [{"id":34,"sg_name":"百合花","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"},{"id":33,"sg_name":"独角兽","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"},{"id":32,"sg_name":"红玫瑰","sg_gold":"1.00","sg_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]
         * act_rules : <p>砸金蛋规则11</p>
         * zjd_gold_num : 1次消耗20.00金币
         * zjd_times : [{"id":18,"title":"1次","times":"11.00"},{"id":19,"title":"10次","times":"10.00"},{"id":20,"title":"100次","times":"100.00"},{"id":21,"title":"自动砸","times":"1.00"}]
         * money : 8000.00
         */

        private String act_rules;
        private String zjd_gold_num;
        private String money;
        private List<AwardListBean> awardList;
        private List<ZjdTimesBean> zjd_times;

        public String getAct_rules() {
            return act_rules;
        }

        public void setAct_rules(String act_rules) {
            this.act_rules = act_rules;
        }

        public String getZjd_gold_num() {
            return zjd_gold_num;
        }

        public void setZjd_gold_num(String zjd_gold_num) {
            this.zjd_gold_num = zjd_gold_num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public List<AwardListBean> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListBean> awardList) {
            this.awardList = awardList;
        }

        public List<ZjdTimesBean> getZjd_times() {
            return zjd_times;
        }

        public void setZjd_times(List<ZjdTimesBean> zjd_times) {
            this.zjd_times = zjd_times;
        }

        public static class AwardListBean {
            /**
             * id : 34
             * sg_name : 百合花
             * sg_gold : 1.00
             * sg_pic : http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
             */

            private int id;
            private String sg_name;
            private String sg_gold;
            private String sg_pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSg_name() {
                return sg_name;
            }

            public void setSg_name(String sg_name) {
                this.sg_name = sg_name;
            }

            public String getSg_gold() {
                return sg_gold;
            }

            public void setSg_gold(String sg_gold) {
                this.sg_gold = sg_gold;
            }

            public String getSg_pic() {
                return sg_pic;
            }

            public void setSg_pic(String sg_pic) {
                this.sg_pic = sg_pic;
            }
        }

        public static class ZjdTimesBean {
            /**
             * id : 18
             * title : 1次
             * times : 11.00
             */

            private int id;
            private String title;
            private String times;
            private String type;


            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTimes() {
                return times;
            }

            public void setTimes(String times) {
                this.times = times;
            }
        }
    }
}
