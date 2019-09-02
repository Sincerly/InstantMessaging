package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/30 0030
 */
public class RoomMusicListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"uid":21,"id":1,"room_id":"37","music_name":"最美的期待","music_url":"http://chitchat.com/uploads/files/20190722/a933afae1c7f0b3ad7efc14b66b44638.mp3","a_name":"周笔畅"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 21
         * id : 1
         * room_id : 37
         * music_name : 最美的期待
         * music_url : http://chitchat.com/uploads/files/20190722/a933afae1c7f0b3ad7efc14b66b44638.mp3
         * a_name : 周笔畅
         */

        private int uid;
        private int id;
        private String room_id;
        private String music_name;
        private String music_url;
        private String a_name;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getMusic_name() {
            return music_name;
        }

        public void setMusic_name(String music_name) {
            this.music_name = music_name;
        }

        public String getMusic_url() {
            return music_url;
        }

        public void setMusic_url(String music_url) {
            this.music_url = music_url;
        }

        public String getA_name() {
            return a_name;
        }

        public void setA_name(String a_name) {
            this.a_name = a_name;
        }
    }
}
