package com.ysxsoft.imtalk.bean;

import java.util.List;

public class WeekStarBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"week":[{"id":38,"ws_name":"百合花","ws_gold":"1.00","ws_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}],"last_star":[{"uid":9,"icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","nickname":"李四"}],"last_star1":[{"id":2283,"award_name":"独角兽","award_gold":"4.00"}],"pxgz":"评选规则","next_gift":[{"id":32,"ws_name":"百合花","ws_gold":"1.00","ws_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]}
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
         * week : [{"id":38,"ws_name":"百合花","ws_gold":"1.00","ws_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]
         * last_star : [{"uid":9,"icon":"http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","nickname":"李四"}]
         * last_star1 : [{"id":2283,"award_name":"独角兽","award_gold":"4.00"}]
         * pxgz : 评选规则
         * next_gift : [{"id":32,"ws_name":"百合花","ws_gold":"1.00","ws_pic":"http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png"}]
         */

        private String pxgz;
        private List<WeekBean> week;
        private List<LastStarBean> last_star;
        private List<LastStar1Bean> last_star1;
        private List<NextGiftBean> next_gift;

        public String getPxgz() {
            return pxgz;
        }

        public void setPxgz(String pxgz) {
            this.pxgz = pxgz;
        }

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }

        public List<LastStarBean> getLast_star() {
            return last_star;
        }

        public void setLast_star(List<LastStarBean> last_star) {
            this.last_star = last_star;
        }

        public List<LastStar1Bean> getLast_star1() {
            return last_star1;
        }

        public void setLast_star1(List<LastStar1Bean> last_star1) {
            this.last_star1 = last_star1;
        }

        public List<NextGiftBean> getNext_gift() {
            return next_gift;
        }

        public void setNext_gift(List<NextGiftBean> next_gift) {
            this.next_gift = next_gift;
        }

        public static class WeekBean {
            /**
             * id : 38
             * ws_name : 百合花
             * ws_gold : 1.00
             * ws_pic : http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
             */

            private int id;
            private String ws_name;
            private String ws_gold;
            private String ws_pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getWs_name() {
                return ws_name;
            }

            public void setWs_name(String ws_name) {
                this.ws_name = ws_name;
            }

            public String getWs_gold() {
                return ws_gold;
            }

            public void setWs_gold(String ws_gold) {
                this.ws_gold = ws_gold;
            }

            public String getWs_pic() {
                return ws_pic;
            }

            public void setWs_pic(String ws_pic) {
                this.ws_pic = ws_pic;
            }
        }

        public static class LastStarBean {
            /**
             * uid : 9
             * icon : http://chitchat.com/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
             * nickname : 李四
             */

            private int uid;
            private String icon;
            private String nickname;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }

        public static class LastStar1Bean {
            /**
             * id : 2283
             * award_name : 独角兽
             * award_gold : 4.00
             */

            private int id;
            private String award_name;
            private String award_gold;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAward_name() {
                return award_name;
            }

            public void setAward_name(String award_name) {
                this.award_name = award_name;
            }

            public String getAward_gold() {
                return award_gold;
            }

            public void setAward_gold(String award_gold) {
                this.award_gold = award_gold;
            }
        }

        public static class NextGiftBean {
            /**
             * id : 32
             * ws_name : 百合花
             * ws_gold : 1.00
             * ws_pic : http://chitchat.com/uploads/images/20190703/6734e2893272b860e971e744968b60f6.png
             */

            private int id;
            private String ws_name;
            private String ws_gold;
            private String ws_pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getWs_name() {
                return ws_name;
            }

            public void setWs_name(String ws_name) {
                this.ws_name = ws_name;
            }

            public String getWs_gold() {
                return ws_gold;
            }

            public void setWs_gold(String ws_gold) {
                this.ws_gold = ws_gold;
            }

            public String getWs_pic() {
                return ws_pic;
            }

            public void setWs_pic(String ws_pic) {
                this.ws_pic = ws_pic;
            }
        }
    }
}
