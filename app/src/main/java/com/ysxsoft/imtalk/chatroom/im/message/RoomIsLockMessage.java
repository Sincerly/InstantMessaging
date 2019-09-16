package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 房间是否加锁  是否纯净模式  是否开启公屏
 */
@MessageTag(value = "SM:RIsLockMsg", flag = MessageTag.NONE)
public class RoomIsLockMessage extends MessageContent {
    private final static String TAG = RoomIsLockMessage.class.getSimpleName();
    private String isLock;
    private String isFair;
    private String isPure;

    public RoomIsLockMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setIsLock(jsonObj.optString("isLock"));
            setIsFair(jsonObj.optString("isFair"));
            setIsPure(jsonObj.optString("isPure"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getIsFair() {
        return isFair;
    }

    public void setIsFair(String isFair) {
        this.isFair = isFair;
    }

    public String getIsPure() {
        return isPure;
    }

    public void setIsPure(String isPure) {
        this.isPure = isPure;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isLock",getIsLock());
            jsonObject.put("isFair",getIsFair());
            jsonObject.put("isPure",getIsPure());
            byte[] bytes = jsonObject.toString().getBytes("UTF-8");
            return bytes;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RoomIsLockMessage(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isLock);
        dest.writeString(this.isFair);
        dest.writeString(this.isPure);
    }

    protected RoomIsLockMessage(Parcel in) {
        this.isLock = in.readString();
        this.isFair = in.readString();
        this.isPure = in.readString();
    }

    public static final Creator<RoomIsLockMessage> CREATOR = new Creator<RoomIsLockMessage>() {
        @Override
        public RoomIsLockMessage createFromParcel(Parcel source) {
            return new RoomIsLockMessage(source);
        }

        @Override
        public RoomIsLockMessage[] newArray(int size) {
            return new RoomIsLockMessage[0];
        }
    };
}
