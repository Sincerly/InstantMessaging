package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;
import android.util.Log;

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
@MessageTag(value = "SM:RNChangeMsg", flag = MessageTag.NONE)
public class RoomNoticeChangedMessage  extends MessageContent {
    private final String TAG = RoomNoticeChangedMessage.class.getSimpleName();
    private String roomDesc;

    public RoomNoticeChangedMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setRoomDesc(jsonObj.optString("roomDesc"));
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getRoomDesc() {
        return roomDesc == null ? "" : roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("roomDesc",getRoomDesc());
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

    public RoomNoticeChangedMessage(){

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.roomDesc);
    }

    protected RoomNoticeChangedMessage(Parcel in){
        this.roomDesc=in.readString();
    }

    public static final Creator<RoomNoticeChangedMessage> CREATOR = new Creator<RoomNoticeChangedMessage>() {
        @Override
        public RoomNoticeChangedMessage createFromParcel(Parcel source) {
            return new RoomNoticeChangedMessage(source);
        }

        @Override
        public RoomNoticeChangedMessage[] newArray(int size) {
            return new RoomNoticeChangedMessage[0];
        }
    };
}
