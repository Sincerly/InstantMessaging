package com.ysxsoft.imtalk.bean;

/**
 * Create By 胡
 * on 2019/7/22 0022
 */
public class SignRuleBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"sign_rule":"<p>签到规则11<\/p>","pic":"http://chitchat.com/uploads/images/20190715/21641f14537037b0b69bcaf4e574de18.png","act_desc":"<p>签到活动详情<\/p>","desc":"连续签到7天  即可获得礼物哦~"}
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
         * sign_rule : <p>签到规则11</p>
         * pic : http://chitchat.com/uploads/images/20190715/21641f14537037b0b69bcaf4e574de18.png
         * act_desc : <p>签到活动详情</p>
         * desc : 连续签到7天  即可获得礼物哦~
         */

        private String sign_rule;
        private String pic;
        private String act_desc;
        private String desc;

        public String getSign_rule() {
            return sign_rule;
        }

        public void setSign_rule(String sign_rule) {
            this.sign_rule = sign_rule;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAct_desc() {
            return act_desc;
        }

        public void setAct_desc(String act_desc) {
            this.act_desc = act_desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
