package com.ysxsoft.imtalk.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
public class BankListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":1,"bank_name":"招商银行"},{"id":2,"bank_name":"建设银行"}]
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

    public static class DataBean implements IPickerViewData{
        /**
         * id : 1
         * bank_name : 招商银行
         */

        private int id;
        private String bank_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        @Override
        public String getPickerViewText() {
            return getBank_name();
        }
    }
}
