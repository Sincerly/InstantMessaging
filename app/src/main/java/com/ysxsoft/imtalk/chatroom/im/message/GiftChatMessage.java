package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 礼物
 */
@MessageTag(value = "SM:GiftChatMsg", flag = MessageTag.NONE)
public class GiftChatMessage extends MessageContent {
    private final static String TAG = GiftChatMessage.class.getSimpleName();

    private String name;
    private String toName;
    private String giftName;
    private String giftPic;
    private String giftNum;

    public GiftChatMessage() {
    }

    public GiftChatMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setName(jsonObj.optString("name"));
            setToName(jsonObj.optString("toName"));
            setGiftName(jsonObj.optString("giftName"));
            setGiftPic(jsonObj.optString("giftPic"));
            setGiftNum(jsonObj.optString("giftNum"));
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
            jsonObj.put("toName", getToName());
            jsonObj.put("giftName", getGiftName());
            jsonObj.put("giftPic", getGiftPic());
            jsonObj.put("giftNum", getGiftNum());
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
        dest.writeString(this.toName);
        dest.writeString(this.giftName);
        dest.writeString(this.giftPic);
        dest.writeString(this.giftNum);
    }

    protected GiftChatMessage(Parcel in) {
        this.name = in.readString();
        this.toName = in.readString();
        this.giftName = in.readString();
        this.giftPic = in.readString();
        this.giftNum = in.readString();
    }

    public static final Creator<GiftChatMessage> CREATOR = new Creator<GiftChatMessage>() {
        @Override
        public GiftChatMessage createFromParcel(Parcel source) {
            return new GiftChatMessage(source);
        }

        @Override
        public GiftChatMessage[] newArray(int size) {
            return new GiftChatMessage[size];
        }
    };

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToName() {
        return toName == null ? "" : toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getGiftName() {
        return giftName == null ? "" : giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPic() {
        return giftPic == null ? "" : giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getGiftNum() {
        return giftNum == null ? "" : giftNum;
    }

    public void setGiftNum(String giftNum) {
        this.giftNum = giftNum;
    }
}
