package com.ysxsoft.imtalk.chatroom.net;

import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo;
import com.ysxsoft.imtalk.chatroom.model.RoomUserListBean;
import com.ysxsoft.imtalk.chatroom.net.model.CreateRoomResult;
import com.ysxsoft.imtalk.chatroom.net.model.Result;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SealMicService {

    /**
     * 创建房间
     *
     * @return
     */
    @POST(SealMicUrls.CREATE_ROOM)
    Call<Result<CreateRoomResult>> createRoom(@Query("uid")String uid);

    /**
     * 获取房间详细信息
     *
     * @param roomId
     * @return
     */
    @POST(SealMicUrls.ROOM_DETAIL)
    Call<Result<DetailRoomInfo>> getRoomDetailInfo(@Query("room_id") String roomId);


    /**
     * 获取房间用户列表
     *
     * @param roomId
     * @return
     */
    @GET(SealMicUrls.ROOM_USER_LIST)
    Call<Result<List<RoomUserListBean>>> getRoomUserList(@Query("roomId") String roomId);

    /**
     * 销毁房间
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.DESTROY_ROOM)
    Call<Result<Boolean>> destroyRoom(@Body RequestBody body);

    /**
     * 加入聊天室
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.JOIN_ROOM)
    Call<Result<DetailRoomInfo>> joinChatRoom(@Body RequestBody body);

    /**
     * 离开聊天室
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.LEAVE_ROOM)
    Call<Result<Boolean>> leaveChatRoom(@Body RequestBody body);

    /**
     * 加入聊天麦位
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.JOIN_MIC)
    Call<Result<Boolean>> joinChatMic(@Body RequestBody body);

    /**
     * 离开聊天麦位
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.LEAVE_MIC)
    Call<Result<Boolean>> leaveChatMic(@Body RequestBody body);

    /**
     * 更高聊天麦位
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.CHANGE_MIC)
    Call<Result<Boolean>> changeChatMicPosition(@Body RequestBody body);

    /**
     * 控制聊天麦位
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.CONTROL_MIC)
    Call<Result<Boolean>> controlChatMic(@Body RequestBody body);

    /**
     * 设置房间背景
     *
     * @param body
     * @return
     */
    @POST(SealMicUrls.SET_ROOM_BACKGROUND)
    Call<Result<Boolean>> setRoomBackground(@Body RequestBody body);
}
