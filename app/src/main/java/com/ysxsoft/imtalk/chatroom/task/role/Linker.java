package com.ysxsoft.imtalk.chatroom.task.role;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.task.AuthManager;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;

import java.util.ArrayList;
import java.util.List;


public class Linker extends Role {
    private MicPositionsBean currentMicInfo;

    public Linker() {
    }

    @Override
    public List<MicBehaviorType> getBehaviorList(int micPosition) {
        MicPositionsBean micPositionInfo = getMicInfoByPosition(micPosition);
        List<MicBehaviorType> behaviorList = new ArrayList<>();
        String currentUserId = AuthManager.getInstance().getCurrentUserId();
        if (micPositionInfo != null && currentUserId != null) {
            String micState = micPositionInfo.getIs_wheat();
            String micUserId = micPositionInfo.getUid();

            // 当麦位上没有用户且非锁麦下课进行跳麦
            if (TextUtils.isEmpty(micUserId) && !"0".equals(micState)) {
                behaviorList.add(MicBehaviorType.JumpToMic);//跳麦
            } else if (currentMicInfo.getSort()-1 == micPosition) {
                behaviorList.add(MicBehaviorType.JumpDownMic);
            }
        }
        return behaviorList;
    }

    @Override
    public void perform(MicBehaviorType micBehaviorType, int targetPosition, String targetUserId, ResultCallback<Boolean> callback) {
        RoomManager roomManager = RoomManager.getInstance();
        switch (micBehaviorType) {
            case JumpToMic:
                if (currentMicInfo != null) {
                    int fromPosition = currentMicInfo.getSort()-1;
//                    roomManager.changeMicPosition(fromPosition, targetPosition, callback);
                    break;
                }
            case JumpDownMic:
//                roomManager.leaveMic(targetPosition, callback);
                break;
        }

    }

    @Override
    public boolean hasVoiceChatPermission() {
        if (currentMicInfo != null) {
            String state = currentMicInfo.getIs_lock_wheat();
            String is_oc_wheat = currentMicInfo.getIs_oc_wheat();
            if (!"0".equals(state) && "0".equals(is_oc_wheat)) {//没锁麦 切没闭麦
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRoomSettingPermission() {
        return false;
    }

    @Override
    public boolean isSameRole(Role role) {
        if (role instanceof Linker) {
            Linker linker = (Linker) role;
            // 当同样为连麦者且可发言状态一致时认为是同一角色
            if (linker.hasVoiceChatPermission() == hasVoiceChatPermission()) {
                return true;
            }
        }
        return false;
    }

    public MicPositionsBean getMicPositionInfo() {
        return currentMicInfo;
    }

    public void setMicPositionInfo(MicPositionsBean micPositionInfo) {
        currentMicInfo = micPositionInfo;
    }

    @Override
    public boolean equals(@Nullable Object target) {
        if (target instanceof Linker) {
            Linker targetLinker = (Linker) target;
            if (currentMicInfo != null && targetLinker.getMicPositionInfo() != null) {
                MicPositionsBean targetPosition = targetLinker.getMicPositionInfo();
                return currentMicInfo.getSort()-1 == targetPosition.getSort()-1
                        && !"0".equals(currentMicInfo.getUid())
                        && currentMicInfo.getUid().equals(targetPosition.getUid())
                        && currentMicInfo.getIs_lock_wheat().equals(targetPosition.getIs_lock_wheat())
                        && currentMicInfo.getIs_wheat().equals(targetPosition.getIs_wheat());
            }
        }
        return super.equals(target);
    }
}
