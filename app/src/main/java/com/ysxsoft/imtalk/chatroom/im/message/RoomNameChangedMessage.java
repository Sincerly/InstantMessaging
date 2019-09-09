package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import com.ysxsoft.imtalk.chatroom.utils.log.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Create By 胡
 * on 2019/9/9 0009
 */
@MessageTag(value = "SM:RNameChangeMsg", flag = MessageTag.NONE)
public class RoomNameChangedMessage extends MessageContent {
    private final String TAG = RoomNameChangedMessage.class.getSimpleName();

    private String roomName;

    public RoomNameChangedMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setRoomName(jsonObj.optString("roomName"));
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("roomName",getRoomName());
            byte[] bytes = jsonObj.toString().getBytes("UTF-8");
            return bytes;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RoomNameChangedMessage(){

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.roomName);
    }

    protected RoomNameChangedMessage(Parcel in){
        this.roomName=in.readString();
    }

    public static final Creator<RoomNameChangedMessage> CREATOR = new Creator<RoomNameChangedMessage>() {
        @Override
        public RoomNameChangedMessage createFromParcel(Parcel source) {
            return new RoomNameChangedMessage(source);
        }

        @Override
        public RoomNameChangedMessage[] newArray(int size) {
            return new RoomNameChangedMessage[0];
        }
    };
}
