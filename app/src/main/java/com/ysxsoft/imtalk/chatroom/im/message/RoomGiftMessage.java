package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
@MessageTag(value = "SM:RoomGiftMsg", flag = MessageTag.NONE)
public class RoomGiftMessage extends MessageContent {
    private final static String TAG = RoomGiftMessage.class.getSimpleName();
    private int position;
    private List<Integer> toPosition;
    private String giftUrl;
    private String staticUrl;

    public RoomGiftMessage() {
    }

    public RoomGiftMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setPosition(jsonObj.optInt("position"));
            JSONArray jsonArray=jsonObj.optJSONArray("toPosition");
            List<Integer> mics=new ArrayList<>();
            for (int i = 0; i <jsonArray.length(); i++) {
                mics.add(jsonArray.optInt(i));
            }
            setToPosition(mics);
            setGiftUrl(jsonObj.optString("giftUrl"));
            setStaticUrl(jsonObj.optString("staticUrl"));
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

    public List<Integer> getToPosition() {
        return toPosition;
    }

    public void setToPosition(List<Integer> toPosition) {
        this.toPosition = toPosition;
    }

    public String getGiftUrl() {
        return giftUrl == null ? "" : giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getStaticUrl() {
        return staticUrl == null ? "" : staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("position", getPosition());
            String json= JSON.toJSONString(getToPosition());
            com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(json);
            JSONArray array=new JSONArray(jsonArray.toJSONString());
            jsonObj.put("toPosition", array);
            jsonObj.put("staticUrl", getStaticUrl());
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
        dest.writeInt(this.position);
        dest.writeList(this.toPosition);
        dest.writeString(this.giftUrl);
        dest.writeString(this.staticUrl);
    }

    protected RoomGiftMessage(Parcel in) {
        this.position = in.readInt();
        this.toPosition = new ArrayList<Integer>();
        in.readList(this.toPosition, Integer.class.getClassLoader());
        this.giftUrl = in.readString();
        this.staticUrl = in.readString();
    }

    public static final Creator<RoomGiftMessage> CREATOR = new Creator<RoomGiftMessage>() {
        @Override
        public RoomGiftMessage createFromParcel(Parcel source) {
            return new RoomGiftMessage(source);
        }

        @Override
        public RoomGiftMessage[] newArray(int size) {
            return new RoomGiftMessage[size];
        }
    };
}
