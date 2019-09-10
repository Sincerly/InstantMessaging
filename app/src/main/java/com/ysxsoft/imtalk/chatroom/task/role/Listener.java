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
