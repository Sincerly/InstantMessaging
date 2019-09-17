package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 礼物
 */
@MessageTag(value = "SM:EggChatMsg", flag = MessageTag.NONE)
public class EggChatMessage extends MessageContent {
    private final static String TAG = EggChatMessage.class.getSimpleName();

    private String name;
    private String giftName;
    private String giftPrice;

    public EggChatMessage() {
    }

    public EggChatMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setName(jsonObj.optString("name"));
            setGiftName(jsonObj.optString("giftName"));
            setGiftPrice(jsonObj.optString("giftPrice"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", getName());
            jsonObj.put("giftName", getGiftName());
            jsonObj.put("giftPrice", getGiftPrice());
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
        dest.writeString(this.name);
        dest.writeString(this.giftName);
        dest.writeString(this.giftPrice);
    }

    protected EggChatMessage(Parcel in) {
        this.name = in.readString();
        this.giftName = in.readString();
        this.giftPrice = in.readString();
    }

    public static final Creator<EggChatMessage> CREATOR = new Creator<EggChatMessage>() {
        @Override
        public EggChatMessage createFromParcel(Parcel source) {
            return new EggChatMessage(source);
        }

        @Override
        public EggChatMessage[] newArray(int size) {
            return new EggChatMessage[size];
        }
    };

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiftName() {
        return giftName == null ? "" : giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPrice() {
        return giftPrice == null ? "" : giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }
}
