package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


/**
 * 设置管理员
 */
@MessageTag(value = "SM:RAdminMsg", flag = MessageTag.NONE)
public class RoomAdminChangeMessage extends MessageContent {
    private final static String TAG = RoomAdminChangeMessage.class.getSimpleName();
    private String isAdmin;
    private String cmd;// 1 添加管理员  2 移除管理员

    public RoomAdminChangeMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setIsAdmin(jsonObj.optString("isAdmin"));
            setIsAdmin(jsonObj.optString("cmd"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isAdmin", getIsAdmin());
            jsonObject.put("cmd", getCmd());
            byte[] bytes = jsonObject.toString().getBytes("UTF-8");
            return bytes;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RoomAdminChangeMessage(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isAdmin);
        dest.writeString(this.cmd);
    }

    protected RoomAdminChangeMessage(Parcel in) {
        this.isAdmin = in.readString();
        this.cmd = in.readString();
    }

    public static final Creator<RoomAdminChangeMessage> CREATOR = new Creator<RoomAdminChangeMessage>() {
        @Override
        public RoomAdminChangeMessage createFromParcel(Parcel source) {
            return new RoomAdminChangeMessage(source);
        }

        @Override
        public RoomAdminChangeMessage[] newArray(int size) {
            return new RoomAdminChangeMessage[0];
        }
    };
}
