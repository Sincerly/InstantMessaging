package com.ysxsoft.imtalk.chatroom.task.role;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;

import java.util.ArrayList;
import java.util.List;

public class Listener extends Role {

    public Listener() {
    }

    @Override
    public List<MicBehaviorType> getBehaviorList(int micPosition) {
        MicPositionsBean micPositionInfo = getMicInfoByPosition(micPosition);
        List<MicBehaviorType> behaviorList = new ArrayList<>();
        if (micPositionInfo != null) {
            String micUserId = micPositionInfo.getUid();
            String micState = micPositionInfo.getIs_lock_wheat();

            // 麦位上没有人且非锁定状态时
            if(TextUtils.isEmpty(micUserId) && !"0".equals(micState)){
                behaviorList.add(MicBehaviorType.JumpOnMic);//上麦
            }
        }
        return behaviorList;
    }

    @Override
    public void perform(MicBehaviorType micBehaviorType, int targetPosition, String targetUserId, ResultCallback<Boolean> callback) {
        RoomManager roomManager = RoomManager.getInstance();
        switch (micBehaviorType) {
            case JumpOnMic:
//                roomManager.joinMic(targetPosition, callback);
                break;
        }
    }

    @Override
    public boolean hasVoiceChatPermission() {
        return false;
    }

    @Override
    public boolean hasRoomSettingPermission() {
        return false;
    }

    @Override
    public boolean isSameRole(Role role) {
        return role instanceof Listener;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Listener) {
            return true;
        }
        return super.equals(obj);
    }
}
