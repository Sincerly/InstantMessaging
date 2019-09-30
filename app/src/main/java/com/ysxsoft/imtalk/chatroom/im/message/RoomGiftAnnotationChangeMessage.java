package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 房间礼物特效变动消息
 */
@MessageTag(value = "SM:RANtfyMsg", flag = MessageTag.NONE)
public class RoomGiftAnnotationChangeMessage extends MessageContent {
    private final static String TAG = RoomGiftAnnotationChangeMessage.class.getSimpleName();
    private String roomTex;

    public RoomGiftAnnotationChangeMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setRoomTex(jsonObj.optString("roomTex"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getRoomTex() {
        return roomTex;
    }

    public void setRoomTex(String roomTex) {
        this.roomTex = roomTex;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomTex", getRoomTex());
            byte[] bytes = jsonObject.toString().getBytes("UTF-8");
            return bytes;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RoomGiftAnnotationChangeMessage(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roomTex);
    }

    protected RoomGiftAnnotationChangeMessage(Parcel in) {
        this.roomTex = in.readString();
    }

    public static final Creator<RoomGiftAnnotationChangeMessage> CREATOR = new Creator<RoomGiftAnnotationChangeMessage>() {
        @Override
        public RoomGiftAnnotationChangeMessage createFromParcel(Parcel source) {
            return new RoomGiftAnnotationChangeMessage(source);
        }

        @Override
        public RoomGiftAnnotationChangeMessage[] newArray(int size) {
            return new RoomGiftAnnotationChangeMessage[0];
        }
    };
}
