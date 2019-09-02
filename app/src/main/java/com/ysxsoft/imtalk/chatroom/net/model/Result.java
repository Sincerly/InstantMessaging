package com.ysxsoft.imtalk.chatroom.net.model;

/**
 * 网络请求结果基础类
 *
 * @param <R> 请求结果的实体类
 */
public class Result<R> {
    private Data<R> data;

    private int code;
    private String msg;

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

    public Data<R> getData() {
        return data;
    }

    public void setData(Data<R> data) {
        this.data = data;
    }

    /**
     * 获取请求结果中的的实体类
     *
     * @return R
     */
    public R getDataResult() {
        if (data != null) {
            return data.getResult();
        }

        return null;
    }

    public static class Data<R> {
        public R result;

        public R getResult() {
            return result;
        }

        public void setResult(R result) {
            this.result = result;
        }
    }
}
