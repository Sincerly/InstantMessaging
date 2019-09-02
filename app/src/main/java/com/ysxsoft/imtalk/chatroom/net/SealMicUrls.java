package com.ysxsoft.imtalk.chatroom.net;

public class SealMicUrls {
//    public static final String DOMAIN = "https://api-sealmic.rongcloud.cn/api/v1/";
    public static final String DOMAIN = "http://chitchat.rhhhyy.com/admin.php/api/";

    public static final String CREATE_ROOM = "Roomnew/createRoom";//创建房间（后台对接了融云）

//    public static final String ROOM_DETAIL = "Room/get_room_info";//房间信息
    public static final String ROOM_DETAIL = "Roomnew/getRoomInfo";//房间信息

    public static final String ROOM_USER_LIST = "Room/RoomUserList";//房间成员列表

    public static final String DESTROY_ROOM = "room/destroy";

    public static final String JOIN_ROOM = "Roomnew/joinRoom";//进入房间

    public static final String LEAVE_ROOM = "Room/tCRoom";//提出房间

    public static final String JOIN_MIC = "Room/upDownWheat";//Room/upDownWheat  上麦 下麦

    public static final String LEAVE_MIC = "Room/upDownWheat";//Room/upDownWheat  上麦 下麦

    public static final String CHANGE_MIC = "room/mic/change";//Room/upDownWheat  上麦 下麦

    public static final String CONTROL_MIC = "room/mic/control";

    public static final String SET_ROOM_BACKGROUND = "room/background";//在房间信息的接口中
}
