package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/29 0029
 */
public class MusicListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":5,"music_name":"好好","author_name":"五月天","music_url":"http://chitchat.com/uploads/files/20190722/39f12969be1286b5560f1620792fd860.mp3"},{"id":4,"music_name":"最美的期待","author_name":"周笔畅","music_url":"http://chitchat.com/uploads/files/20190722/a933afae1c7f0b3ad7efc14b66b44638.mp3"}]
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
         * id : 5
         * music_name : 好好
         * author_name : 五月天
         * music_url : http://chitchat.com/uploads/files/20190722/39f12969be1286b5560f1620792fd860.mp3
         */

        private int id;
        private String music_name;
        private String author_name;
        private String music_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMusic_name() {
            return music_name;
        }

        public void setMusic_name(String music_name) {
            this.music_name = music_name;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getMusic_url() {
            return music_url;
        }

        public void setMusic_url(String music_url) {
            this.music_url = music_url;
        }
    }
}
