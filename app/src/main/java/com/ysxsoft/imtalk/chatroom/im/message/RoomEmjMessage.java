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

    public RoomEmjMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setPosition(jsonObj.optInt("p"));
            setImageUrl(jsonObj.optString("image"));
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
        return new byte[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
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
