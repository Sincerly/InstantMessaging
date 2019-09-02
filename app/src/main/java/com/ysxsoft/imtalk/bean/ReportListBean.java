package com.ysxsoft.imtalk.bean;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/30 0030
 */
public class ReportListBean {

    /**
     * code : 0
     * msg : 成功！
     * data : [{"id":18,"title":"低俗"},{"id":19,"title":"辱骂"},{"id":20,"title":"言论不雅"}]
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
         * id : 18
         * title : 低俗
         */

        private String id;
        private String title;
        private boolean isChoosed;

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
