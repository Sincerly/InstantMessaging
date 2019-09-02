package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/8/14 0014
 */
public class HomeHLBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"room_id":43,"title":"嗨聊派对","desc":"随机进入嗨聊房","pic":"http://chitchat.com/uploads/images/20190814/1ccd0afd05ea3703bfc1f7cd6ba003be.png"}
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
         * room_id : 43
         * title : 嗨聊派对
         * desc : 随机进入嗨聊房
         * pic : http://chitchat.com/uploads/images/20190814/1ccd0afd05ea3703bfc1f7cd6ba003be.png
         */

        private int room_id;
        private String title;
        private String desc;
        private String pic;

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
