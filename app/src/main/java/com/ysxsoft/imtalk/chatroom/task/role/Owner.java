package com.ysxsoft.imtalk.chatroom.task.role;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ysxsoft.imtalk.chatroom.constant.ErrorCode;
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo;
import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.model.MicState;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;

import java.util.ArrayList;
import java.util.List;


public class Owner extends Role {
    public Owner() {
    }

    @Override
    public List<MicBehaviorType> getBehaviorList(int micPosition) {
        MicPositionsBean micPositionInfo = getMicInfoByPosition(micPosition);
        List<MicBehaviorType> behaviorList = new ArrayList<>();
        String userId = micPositionInfo.getUid();

        if (micPositionInfo != null) {
            // 根据麦位状态添加可操作的行为
            String micState = micPositionInfo.getIs_lock_wheat();
            String is_block = micPositionInfo.getIs_block();
            // 判断麦位是否有人来进行抱麦和下麦操作
//            if (TextUtils.isEmpty(userId) && !"0".equals(micState)) {
//                behaviorList.add(MicBehaviorType.PickupMic);    //抱麦
//            } else if (!TextUtils.isEmpty(userId)) {
//                behaviorList.add(MicBehaviorType.KickOffMic);   //踢麦
//                behaviorList.add(MicBehaviorType.JumpDownMic);  //下麦
//            }
//
//            // 判断锁麦状态
//            if ("0".equals(is_block)) {
//                behaviorList.add(MicBehaviorType.UnlockMic);        //解锁麦
//            } else {
//                behaviorList.add(MicBehaviorType.LockMic);          //锁麦
//            }
        }

        return behaviorList;
    }

    @Override
    public void perform(MicBehaviorType micBehaviorType, int targetPosition, String targetUserId, ResultCallback<Boolean> callback) {
//        RoomManager.getInstance().controlMicPosition(targetPosition, targetUserId, micBehaviorType.ordinal(), callback);
    }


    @Override
    public void leaveRoom(final ResultCallback<Boolean> callBack) {
        final RoomManager roomManager = RoomManager.getInstance();
        DetailRoomInfo currentRoomInfo = roomManager.getCurrentRoomInfo();
        if (currentRoomInfo == null || TextUtils.isEmpty(currentRoomInfo.getRoomInfo().getRoom_id())) {
            if (callBack != null) {
                callBack.onFail(ErrorCode.ROOM_NOT_JOIN_TO_ROOM.getCode());
            }
            return;
        }

        final String roomId = currentRoomInfo.getRoomInfo().getRoom_id();
//        roomManager.leaveRoom(new ResultCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
////                roomManager.destroyRoom(roomId, callBack);
//            }
//
//            @Override
//            public void onFail(int errorCode) {
//                if (callBack != null) {
//                    callBack.onFail(errorCode);
//                }
//            }
//        });
    }

    @Override
    public boolean hasVoiceChatPermission() {
        return true;
    }

    @Override
    public boolean hasRoomSettingPermission() {
        return true;
    }

    @Override
    public boolean isSameRole(Role role) {
        return role instanceof Owner;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Owner) {
            return true;
        }
        return super.equals(obj);
    }
}
