package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 表情
 */
@MessageTag(value = "SM:RoomEmjMsg", flag = MessageTag.NONE)
public class RoomEmjMessage extends MessageContent {
    private final static String TAG = RoomEmjMessage.class.getSimpleName();
    private int position;
    private String imageUrl;

    public RoomEmjMessage() {
    }

    public RoomEmjMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setPosition(jsonObj.optInt("position"));
            setImageUrl(jsonObj.optString("emjUrl"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("position", getPosition());
            jsonObj.put("emjUrl", getImageUrl());
            byte[] bytes = jsonObj.toString().getBytes("UTF-8");
            return bytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeString(this.imageUrl);
    }

    protected RoomEmjMessage(Parcel in) {
    }

    public static final Creator<RoomEmjMessage> CREATOR = new Creator<RoomEmjMessage>() {
        @Override
        public RoomEmjMessage createFromParcel(Parcel source) {
            return new RoomEmjMessage(source);
        }

        @Override
        public RoomEmjMessage[] newArray(int size) {
            return new RoomEmjMessage[0];
        }
    };
}
