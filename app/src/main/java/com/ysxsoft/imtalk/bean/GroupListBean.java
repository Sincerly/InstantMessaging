package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/8/10 0010
 */
public class GroupListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"groupInfo":[{"gr_status":2,"id":8,"type":"2","group_avatar":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","group_name":"小姐姐1","group_num":4},{"gr_status":2,"id":10,"type":"2","group_avatar":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","group_name":"小姐姐2","group_num":4}],"count":2}
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
         * groupInfo : [{"gr_status":2,"id":8,"type":"2","group_avatar":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","group_name":"小姐姐1","group_num":4},{"gr_status":2,"id":10,"type":"2","group_avatar":"http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png","group_name":"小姐姐2","group_num":4}]
         * count : 2
         */

        private String count;
        private List<GroupInfoBean> groupInfo;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<GroupInfoBean> getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(List<GroupInfoBean> groupInfo) {
            this.groupInfo = groupInfo;
        }

        public static class GroupInfoBean {
            /**
             * gr_status : 2
             * id : 8
             * type : 2
             * group_avatar : http://chitchat.sanzhima.cn/uploads/images/20190720/893faf29df6ae21775b838890933b26c.png
             * group_name : 小姐姐1
             * group_num : 4
             */

            private String gr_status;
            private String id;
            private String type;
            private String group_avatar;
            private String group_name;
            private String group_num;

            public String getGr_status() {
                return gr_status;
            }

            public void setGr_status(String gr_status) {
                this.gr_status = gr_status;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getGroup_avatar() {
                return group_avatar;
            }

            public void setGroup_avatar(String group_avatar) {
                this.group_avatar = group_avatar;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getGroup_num() {
                return group_num;
            }

            public void setGroup_num(String group_num) {
                this.group_num = group_num;
            }
        }
    }
}
