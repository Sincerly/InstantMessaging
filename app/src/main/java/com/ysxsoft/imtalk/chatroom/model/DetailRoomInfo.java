package com.ysxsoft.imtalk.chatroom.model;


import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Message;


/**
 * 房间详情信息请求结果
 */
public class DetailRoomInfo extends BaseRoomInfo {

    private List<RoomUserListBean> roomUserList = new ArrayList<>();
    private List<MicPositionsBean> micPositions = new ArrayList<>();
    private List<AdminListInfoBean> adminListInfo = new ArrayList<>();
    private List<Message> messageList = new ArrayList<>();


    public List<RoomUserListBean> getRoomUserList() {
        return roomUserList;
    }

    public void setRoomUserList(List<RoomUserListBean> roomUserList) {
        if (roomUserList!=null) {
            this.roomUserList = roomUserList;
        }
    }

    public List<MicPositionsBean> getMicPositions() {
        return micPositions;
    }

    public void setMicPositions(List<MicPositionsBean> micPositions) {
        if (micPositions != null) {
            this.micPositions = micPositions;
        } else {
            clearMicPositions();
        }
    }

    public void clearMicPositions() {
        this.micPositions.clear();
    }

    public List<RoomUserListBean> getAudiences() {
        return roomUserList;
    }

    public List<AdminListInfoBean> getAdminListInfo() {
        return adminListInfo;
    }

    public void setAdminListInfo(List<AdminListInfoBean> adminListInfo) {
        if (adminListInfo!=null) {
            this.adminListInfo = adminListInfo;
        }
    }

    public void setAudiences(List<RoomUserListBean> roomUserList) {
        if (roomUserList!=null) {
            this.roomUserList = roomUserList;
        }
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
