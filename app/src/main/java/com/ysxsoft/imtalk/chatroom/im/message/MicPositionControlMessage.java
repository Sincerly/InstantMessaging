package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.utils.log.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 麦位控制消息
 */
@MessageTag(value = "SM:MPCtrlMsg", flag = MessageTag.NONE)
public class MicPositionControlMessage extends MessageContent {
    private final static String TAG = MicPositionControlMessage.class.getSimpleName();
    private int cmd;
    private String targetUserId;
    private int targetPosition;
    private List<MicPositionsBean> micPositions;

    public MicPositionControlMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            setCmd(jsonObj.optInt("cmd"));
            setTargetUserId(jsonObj.optString("targetUserId"));
            setTargetPosition(jsonObj.optInt("targetPosition"));
            JSONArray positions = jsonObj.optJSONArray("micPositions");
            List<MicPositionsBean> infoList = new ArrayList<>();
            if (positions != null && positions.length() > 0) {
                for (int i = 0; i < positions.length(); i++) {
                    infoList.add(MicPositionsBean.parseJsonToMicPositionInfo(positions.getJSONObject(i)));
                }
            }
            setMicPositions(infoList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MicBehaviorType getBehaviorType() {
        return MicBehaviorType.valueOf(cmd);
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    public List<MicPositionsBean> getMicPositions() {
        return micPositions;
    }

    public void setMicPositions(List<MicPositionsBean> micPositions) {
        this.micPositions = micPositions;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cmd);
        dest.writeString(this.targetUserId);
        dest.writeInt(this.targetPosition);
        dest.writeTypedList(this.micPositions);
    }

    public MicPositionControlMessage() {
    }

    protected MicPositionControlMessage(Parcel in) {
        this.cmd = in.readInt();
        this.targetUserId = in.readString();
        this.targetPosition = in.readInt();
        this.micPositions = in.createTypedArrayList(MicPositionsBean.CREATOR);
    }

    public static final Creator<MicPositionControlMessage> CREATOR = new Creator<MicPositionControlMessage>() {
        @Override
        public MicPositionControlMessage createFromParcel(Parcel source) {
            return new MicPositionControlMessage(source);
        }

        @Override
        public MicPositionControlMessage[] newArray(int size) {
            return new MicPositionControlMessage[size];
        }
    };
}
