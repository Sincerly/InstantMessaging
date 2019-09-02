package com.ysxsoft.imtalk.chatroom.model;

import android.content.Context;

import com.ysxsoft.imtalk.R;


public enum MicBehaviorType {
    PickupMic,  //抱麦  0
    LockMic,    //锁麦  1
    UnlockMic,  //解锁  2
    ForbidMic,  //禁麦  3
    UnForbidMic, //解麦 4
    KickOffMic, // 踢麦 5
    JumpOnMic,  // 上麦 6
    JumpDownMic, //下麦 7
    JumpToMic, //跳麦  8
    MuteMic,
    UnMuteMic,
    UnKnown;

    public static MicBehaviorType valueOf(int value) {
        for (MicBehaviorType type : MicBehaviorType.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        return UnKnown;
    }

    public String getName(Context context) {
        int ordinal = ordinal();
        if (ordinal == PickupMic.ordinal()) {
            return context.getString(R.string.mic_cmd_pick_mic);
        } else if (ordinal == LockMic.ordinal()) {
            return context.getString(R.string.mic_cmd_lock_mic);
        } else if (ordinal == UnlockMic.ordinal()) {
            return context.getString(R.string.mic_cmd_unlock_mic);
        } else if (ordinal == ForbidMic.ordinal()) {
            return context.getString(R.string.mic_cmd_forbidden);
        } else if (ordinal == UnForbidMic.ordinal()) {
            return context.getString(R.string.mic_cmd_unforbidden);
        } else if (ordinal == JumpDownMic.ordinal()) {
            return context.getString(R.string.mic_cmd_jump_down);
        } else if (ordinal == KickOffMic.ordinal()) {
            return context.getString(R.string.mic_cmd_kick_off);
        }
        return UnKnown.name();
    }
}
