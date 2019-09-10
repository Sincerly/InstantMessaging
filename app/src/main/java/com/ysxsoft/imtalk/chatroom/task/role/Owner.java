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

    /**
     * 有话筒聊天的权限
     *
     * @return
     */
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
