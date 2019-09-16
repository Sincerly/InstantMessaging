package com.ysxsoft.imtalk.chatroom.im;

import android.content.Context;
import android.net.Uri;

import com.ysxsoft.imtalk.chatroom.constant.ErrorCode;
import com.ysxsoft.imtalk.chatroom.im.message.MicPositionChangeMessage;
import com.ysxsoft.imtalk.chatroom.im.message.MicPositionControlMessage;
import com.ysxsoft.imtalk.chatroom.im.message.MicPositionGiftValueMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomBgChangeMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomDestroyNotifyMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomEmjMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomGiftMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomIsActiveMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomLableChangedMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomNameChangedMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomNoticeChangedMessage;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.utils.log.SLog;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Rong IM 业务相关封装
 */
public class IMClient {
    private static final String TAG = IMClient.class.getSimpleName();
    private static IMClient sInstance;
    private final int DEFAULT_MESSAGE_COUNT = -1;//-1代表不拉取历史消息
    private List<RongIMClient.OnReceiveMessageListener> listenerList = new ArrayList<>();

    public static IMClient getInstance() {
        if (sInstance == null) {
            synchronized (IMClient.class) {
                if (sInstance == null) {
                    sInstance = new IMClient();
                }
            }
        }
        return sInstance;
    }

    private IMClient() {
    }

    /**
     * 初始化，需要在使用前初始化一次
     *
     * @param context
     */
    public void init(Context context) {
        /*
         * 初始化 SDK，在整个应用程序全局，只需要调用一次。建议在 Application 继承类中调用。
         */
        // 可在初始 SDK 时直接带入融云 IM 申请的APP KEY
//        RongIMClient.init(context, "lmxuhwagl5dcd", false);

        try {
            RongIMClient.registerMessageType(MicPositionChangeMessage.class); //麦位改变消息
            RongIMClient.registerMessageType(MicPositionControlMessage.class);//麦位控制消息
            RongIMClient.registerMessageType(RoomMemberChangedMessage.class);//房间成员改变消息
            RongIMClient.registerMessageType(RoomBgChangeMessage.class);//背景改变消息
            RongIMClient.registerMessageType(RoomDestroyNotifyMessage.class);//房间销毁消息
            RongIMClient.registerMessageType(RoomIsActiveMessage.class);//房主发送的房间还在活动中的消息

            RongIMClient.registerMessageType(RoomEmjMessage.class);//房间表情动画
            RongIMClient.registerMessageType(RoomGiftMessage.class);//房间礼物动画
            RongIMClient.registerMessageType(RoomNoticeChangedMessage.class);//房间通知
            RongIMClient.registerMessageType(RoomLableChangedMessage.class);//房间标签
            RongIMClient.registerMessageType(RoomNameChangedMessage.class);//房间名称
            RongIMClient.registerMessageType(MicPositionGiftValueMessage.class);//房间礼物值是否显示
        } catch (AnnotationNotFoundException e) {
            SLog.e(TAG, "Failed to register messages!!");
            e.printStackTrace();
        }

        /*
         * 管理消息监听，由于同一时间只能有一个消息监听加入 融云 的消息监听，所以做一个消息管理来做消息路由
         */
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int left) {
                SLog.d(TAG, "onReceived message. tag:" + message.getObjectName());
                synchronized (listenerList) {
                    if (listenerList.size() > 0) {
                        for (RongIMClient.OnReceiveMessageListener listener : listenerList) {
                            boolean result = listener.onReceived(message, left);
                            if (result) {
                                break;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 加入 IM 聊天室
     *
     * @param roomId
     * @param callBack
     */
    public void joinChatRoom(final String roomId, final ResultCallback<String> callBack) {
        RongIMClient.getInstance().joinChatRoom(roomId, DEFAULT_MESSAGE_COUNT, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.onSuccess(roomId);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                SLog.e(SLog.TAG_IM, "join chat room error, error msg:" + errorCode.getMessage() + " , error code" + errorCode.getValue());
                if (callBack != null) {
                    callBack.onFail(ErrorCode.IM_ERROR.getCode());
                }
            }
        });
    }

    /**
     * 离开 IM 聊天室
     *
     * @param roomId
     * @param callBack
     */
    public void quitChatRoom(final String roomId, final ResultCallback<String> callBack) {
        RongIMClient.getInstance().quitChatRoom(roomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.onSuccess(roomId);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                SLog.e(SLog.TAG_IM, "quit chat room error, error msg:" + errorCode.getMessage() + " , error code" + errorCode.getValue());
                if (callBack != null) {
                    callBack.onFail(ErrorCode.IM_ERROR.getCode());
                }
            }
        });
    }

    /**
     * 加入消息监听
     *
     * @param listener
     */
    public void addMessageReceiveListener(RongIMClient.OnReceiveMessageListener listener) {
        synchronized (listenerList) {
            listenerList.add(listener);
        }
    }

    /**
     * 移除消息监听
     *
     * @param listener
     */
    public void removeMessageReceiveListener(RongIMClient.OnReceiveMessageListener listener) {
        synchronized (listenerList) {
            listenerList.remove(listener);
        }
    }

    /**
     * 创建本地显示用户进入房间消息
     *
     * @param userId 进入房间的用户
     * @param roomId 房间id
     * @return
     */
    public Message createLocalEnterRoomMessage(String userId, String roomId,String name,String icon) {
        RoomMemberChangedMessage memberChangedMessage = new RoomMemberChangedMessage();
        memberChangedMessage.setTargetUserId(userId);
        memberChangedMessage.setCmd(1);
        memberChangedMessage.setUserInfo(new UserInfo(userId, name,Uri.parse(icon)));
        return Message.obtain(roomId, Conversation.ConversationType.CHATROOM, memberChangedMessage);
    }
}
