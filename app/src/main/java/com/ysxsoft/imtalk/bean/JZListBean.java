package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/10 0010
 */
public class JZListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : {"count":2,"fmy_list":[{"id":37,"is_fmy":1,"uid":2,"tt_id":"81456162","nickname":"Amor_L","icon":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","ml_level":10,"user_level":1},{"id":38,"is_fmy":0,"uid":21,"tt_id":"5685621","nickname":"我是小嗨嗨8228","icon":"http://chitchat.sanzhima.cn/static/admin/img/icon.png","ml_level":1,"user_level":1}]}
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
         * count : 2
         * fmy_list : [{"id":37,"is_fmy":1,"uid":2,"tt_id":"81456162","nickname":"Amor_L","icon":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","ml_level":10,"user_level":1},{"id":38,"is_fmy":0,"uid":21,"tt_id":"5685621","nickname":"我是小嗨嗨8228","icon":"http://chitchat.sanzhima.cn/static/admin/img/icon.png","ml_level":1,"user_level":1}]
         */

        private int count;
        private List<FmyListBean> fmy_list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<FmyListBean> getFmy_list() {
            return fmy_list;
        }

        public void setFmy_list(List<FmyListBean> fmy_list) {
            this.fmy_list = fmy_list;
        }

        public static class FmyListBean {
            /**
             * id : 37
             * is_fmy : 1
             * uid : 2
             * tt_id : 81456162
             * nickname : Amor_L
             * icon : http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
             * ml_level : 10
             * user_level : 1
             */

            private String id;
            private String is_fmy;
            private String uid;
            private String tt_id;
            private String nickname;
            private String icon;
            private String ml_level;
            private String user_level;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_fmy() {
                return is_fmy;
            }

            public void setIs_fmy(String is_fmy) {
                this.is_fmy = is_fmy;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getTt_id() {
                return tt_id;
            }

            public void setTt_id(String tt_id) {
                this.tt_id = tt_id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getMl_level() {
                return ml_level;
            }

            public void setMl_level(String ml_level) {
                this.ml_level = ml_level;
            }

            public String getUser_level() {
                return user_level;
            }

            public void setUser_level(String user_level) {
                this.user_level = user_level;
            }
        }
    }
}
