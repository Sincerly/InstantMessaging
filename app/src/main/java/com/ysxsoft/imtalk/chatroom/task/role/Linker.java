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
    public boolean hasVoiceChatPermission() {
        if (currentMicInfo != null) {
            String is_oc_wheat = currentMicInfo.getIs_oc_wheat();
            if ("0".equals(is_oc_wheat)) {//没闭麦
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
