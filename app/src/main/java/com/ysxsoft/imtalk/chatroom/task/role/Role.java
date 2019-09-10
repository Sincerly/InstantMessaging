package com.ysxsoft.imtalk.chatroom.task.role;

import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo;
import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;

import java.util.ArrayList;
import java.util.List;


public abstract class Role {
    final String TAG = this.getClass().getSimpleName();

    /**
     * 是否有聊天权限
     * @return
     */
    public abstract boolean hasVoiceChatPermission();

    /**
     * 是否有设置房间权限
     * @return
     */
    public abstract boolean hasRoomSettingPermission();

    /**
     * 角色是否一样
     * @param role
     * @return
     */
    public abstract boolean isSameRole(Role role);

}
