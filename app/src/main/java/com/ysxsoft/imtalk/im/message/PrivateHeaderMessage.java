package com.ysxsoft.imtalk.im.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

@MessageTag(value = "SM:PrivateHeaderMsg", flag = MessageTag.ISCOUNTED|MessageTag.ISPERSISTED)
public class PrivateHeaderMessage extends MessageContent {
    private final static String TAG = PrivateHeaderMessage.class.getSimpleName();
    private String headerUrl;
    private String headerName;
    private String headerNum;
    private String toName;

    public PrivateHeaderMessage() {
    }

    public PrivateHeaderMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setHeaderName(jsonObj.optString("headerName"));
            setHeaderUrl(jsonObj.optString("headerUrl"));
            setHeaderNum(jsonObj.optString("headerNum"));
            setToName(jsonObj.optString("toName"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getHeaderUrl() {
        return headerUrl == null ? "" : headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getHeaderName() {
        return headerName == null ? "" : headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderNum() {
        return headerNum == null ? "" : headerNum;
    }

    public void setHeaderNum(String headerNum) {
        this.headerNum = headerNum;
    }

    public String getToName() {
        return toName == null ? "" : toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("headerName", getHeaderName());
            jsonObj.put("headerNum", getHeaderNum());
            jsonObj.put("headerUrl", getHeaderUrl());
            jsonObj.put("toName", getToName());
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
        dest.writeString(this.headerName);
        dest.writeString(this.headerNum);
        dest.writeString(this.headerUrl);
        dest.writeString(this.toName);
    }

    protected PrivateHeaderMessage(Parcel in) {
        this.headerName = in.readString();
        this.headerNum = in.readString();
        this.headerUrl = in.readString();
        this.toName = in.readString();
    }

    public static final Creator<PrivateHeaderMessage> CREATOR = new Creator<PrivateHeaderMessage>() {
        @Override
        public PrivateHeaderMessage createFromParcel(Parcel source) {
            return new PrivateHeaderMessage(source);
        }

        @Override
        public PrivateHeaderMessage[] newArray(int size) {
            return new PrivateHeaderMessage[0];
        }
    };

    /**
     * 赠送头饰消息
     * @param targetId
     * @param headerNum
     * @param headerName
     * @param picUrl
     * @param toUserName
     */
    public static void sendMessage(String targetId,String headerNum,String headerName,String picUrl,String toUserName){
        //rongExtension.getTargetId()
        PrivateHeaderMessage message=new PrivateHeaderMessage();
        message.setHeaderNum(headerNum);
        message.setHeaderName(headerName);
        message.setHeaderUrl(picUrl);
        message.setToName(toUserName);
        Message m=Message.obtain(targetId, Conversation.ConversationType.PRIVATE,message);
        RongIM.getInstance().sendMessage(m, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                Log.e("tag","onAttached");
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("tag","onSuccess:"+message.toString());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("tag","onError:");
            }
        });
    }
}

