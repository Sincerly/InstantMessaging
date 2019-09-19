package com.ysxsoft.imtalk.bean;

import java.util.ArrayList;

/**
 * Create By 胡
 * on 2019/9/19 0019
 */
public class EventBusBean {

    private ArrayList<RoomMusicListBean.DataBean> list;
    private int position;

    public  EventBusBean(ArrayList<RoomMusicListBean.DataBean> list, int position){
        this.list = list;
        this.position = position;
    }

    public ArrayList<RoomMusicListBean.DataBean> getList() {
        return list;
    }

    public void setList(ArrayList<RoomMusicListBean.DataBean> list) {
        this.list = list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
