package com.ysxsoft.imtalk.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "SM:PrivateGiftMsg", flag = MessageTag.ISCOUNTED|MessageTag.ISPERSISTED)
public class PrivateGiftMessage extends MessageContent {
    private final static String TAG = PrivateGiftMessage.class.getSimpleName();
    private String giftUrl;
    private String giftName;
    private String giftNum;
    private String toName;

    public PrivateGiftMessage() {
    }

    public PrivateGiftMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setGiftName(jsonObj.optString("giftName"));
            setGiftUrl(jsonObj.optString("giftUrl"));
            setGiftNum(jsonObj.optString("giftNum"));
            setToName(jsonObj.optString("toName"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getGiftUrl() {
        return giftUrl == null ? "" : giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getGiftName() {
        return giftName == null ? "" : giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftNum() {
        return giftNum == null ? "" : giftNum;
    }

    public String getToName() {
        return toName == null ? "" : toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setGiftNum(String giftNum) {
        this.giftNum = giftNum;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("giftName", getGiftName());
            jsonObj.put("giftNum", getGiftNum());
            jsonObj.put("giftUrl", getGiftUrl());
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
        dest.writeString(this.giftName);
        dest.writeString(this.giftNum);
        dest.writeString(this.giftUrl);
        dest.writeString(this.toName);
    }

    protected PrivateGiftMessage(Parcel in) {
        this.giftName = in.readString();
        this.giftNum = in.readString();
        this.giftUrl = in.readString();
        this.toName = in.readString();
    }

    public static final Creator<PrivateGiftMessage> CREATOR = new Creator<PrivateGiftMessage>() {
        @Override
        public PrivateGiftMessage createFromParcel(Parcel source) {
            return new PrivateGiftMessage(source);
        }

        @Override
        public PrivateGiftMessage[] newArray(int size) {
            return new PrivateGiftMessage[0];
        }
    };
}

