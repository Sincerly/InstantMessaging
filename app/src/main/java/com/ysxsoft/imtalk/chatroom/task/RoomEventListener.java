package com.ysxsoft.imtalk.chatroom.task;

import com.ysxsoft.imtalk.bean.RoomPublicGiftMessageBean;
import com.ysxsoft.imtalk.chatroom.im.message.RoomPublicGiftMessage;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.task.role.Role;

import java.util.List;

import io.rong.imlib.model.Message;


/**
 * 聊天房间事件监听
 */
public interface RoomEventListener {
    /**
     * 房间人数发生变化
     *
     * @param memberCount
     */
    void onRoomMemberChange(int memberCount);

    /**
     * 房间人数发生变化
     *
     * @param uid
     */
    void onRoomMemberKickChange(String uid);


    /**
     * 麦位信息更新
     *
     * @param micPositionInfoList
     */
    void onMicUpdate(List<MicPositionsBean> micPositionInfoList);

    /**
     * 当有房间内有人说话时，回调所有当前说话人的用户id
     *
     * @param speakUserIdList
     */
    void onRoomMicSpeak(List<String> speakUserIdList);

    /**
     * 当有新的聊天消息
     *
     * @param message
     */
    void onMessageEvent(Message message);

    /**
     * 发送消息失败
     *
     * @param message
     * @param errorCode
     */
    void onSendMessageError(Message message, int errorCode);

    /**
     * 房间背景更新
     *
     * @param bgId
     */
    void onRoomBgChanged(String bgId);

    /**
     * 当前角色发生改变
     *
     * @param role
     */
    void onRoleChanged(Role role);

    /**
     * 被提出房间
     */
    void onKickOffRoom();

    /**
     * 长时间网络异常时退出房间
     */
    void onErrorLeaveRoom();

    /**
     * 当房间超出了最长存在时间
     */
    void onRoomExistOverTimeLimit();

    /**
     * 表情
     *
     * @param p
     * @param url
     */
    void onRoomEmj(int p, String url);

    /**
     * 礼物
     *
     * @param p
     * @param toP
     * @param giftUrl
     * @param staticUrl
     */
    void onRoomGift(int p, List<Integer> toP,String giftUrl,String staticUrl);

    /**
     * 房间通知
     */
    void onRoomNotice(String room_desc);

    /**
     * 房间标签
     */
    void onRoomLable(String room_lable);

    /**
     * 房间名称
     */
    void onRoomName(String room_name);

    /**
     * 是否显示礼物值
     */
    void onGiftValue(int cmd, List<MicPositionsBean> micPositionInfoList, String houseOwnerValue);

    /**
     * 礼物消息
     * @param roomPublicGiftMessageBean
     */
    void onGiftMessage(RoomPublicGiftMessageBean roomPublicGiftMessageBean);

    /**
     * 砸金蛋消息
     * @param nickname
     * @param giftName
     * @param goldNum
     */
    void onGoldMessage(String nickname,String giftName,String goldNum);
//    void onGoldMessage(RoomPublicGiftMessageBean roomPublicGiftMessageBean);

    /**
     *  房间是否加锁  是否纯净模式  是否开启公屏
     */
    void onIsLock(String isLock,String isFair,String isPure);

    /**
     * 设置管理员
     */
    void setManager(String uid,String cmd);

}
