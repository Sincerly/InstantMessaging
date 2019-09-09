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
@MessageTag(value = "SM:RLChangeMsg", flag = MessageTag.NONE)
public class RoomLableChangedMessage extends MessageContent {
    private final String TAG = RoomLableChangedMessage.class.getSimpleName();
    private String roomLable;

    public RoomLableChangedMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setRoomLable(jsonObj.optString("roomLable"));
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getRoomLable() {
        return roomLable;
    }

    public void setRoomLable(String roomLable) {
        this.roomLable = roomLable;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("roomLable",getRoomLable());
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

    public RoomLableChangedMessage(){

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.roomLable);
    }

    protected RoomLableChangedMessage(Parcel in){
        this.roomLable=in.readString();
    }

    public static final Creator<RoomLableChangedMessage> CREATOR = new Creator<RoomLableChangedMessage>() {
        @Override
        public RoomLableChangedMessage createFromParcel(Parcel source) {
            return new RoomLableChangedMessage(source);
        }

        @Override
        public RoomLableChangedMessage[] newArray(int size) {
            return new RoomLableChangedMessage[0];
        }
    };
}
