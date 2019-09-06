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
    ArrayList<MicBehaviorType> behaviorTypes;

    public abstract List<MicBehaviorType> getBehaviorList(int micPosition);

    MicPositionsBean getMicInfoByPosition(int position) {
        DetailRoomInfo currentRoomInfo = RoomManager.getInstance().getCurrentRoomInfo();
        if (currentRoomInfo != null && currentRoomInfo.getMicPositions() != null) {
            List<MicPositionsBean> micPositions = currentRoomInfo.getMicPositions();

            if (micPositions != null) {
                for (MicPositionsBean info : micPositions) {
                    if (info.getSort()-1 == position) {
                        return info;
                    }
                }

                // 若当前麦位信息列表中没有该位置上的信息，则该位置为空
                MicPositionsBean emptyInfo = new MicPositionsBean();
                emptyInfo.setRoom_id(currentRoomInfo.getRoomInfo().getRoom_id());
                emptyInfo.setSort(position);
                emptyInfo.setIs_lock_wheat("0");
                emptyInfo.setIs_wheat("0");
                emptyInfo.setUid(currentRoomInfo.getRoomInfo().getUid());
                return emptyInfo;
            }
        }

        return null;
    }

    public abstract void perform(MicBehaviorType micBehaviorType, int targetPosition, String targetUserId, final ResultCallback<Boolean> callback);

    public void leaveRoom(ResultCallback<Boolean> callBack) {
//        RoomManager.getInstance().leaveRoom(callBack);
    }

    public abstract boolean hasVoiceChatPermission();

    public abstract boolean hasRoomSettingPermission();

    public abstract boolean isSameRole(Role role);

}
