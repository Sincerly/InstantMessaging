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

@MessageTag(value = "SM:PrivateCarMsg", flag = MessageTag.ISCOUNTED|MessageTag.ISPERSISTED)
public class PrivateCarMessage extends MessageContent {
    private final static String TAG = PrivateCarMessage.class.getSimpleName();
    private String carUrl;
    private String carName;
    private String carNum;
    private String toName;

    public PrivateCarMessage() {
    }

    public PrivateCarMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setCarName(jsonObj.optString("carName"));
            setCarUrl(jsonObj.optString("carUrl"));
            setCarNum(jsonObj.optString("carNum"));
            setToName(jsonObj.optString("toName"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getCarUrl() {
        return carUrl == null ? "" : carUrl;
    }

    public void setCarUrl(String carUrl) {
        this.carUrl = carUrl;
    }

    public String getCarName() {
        return carName == null ? "" : carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNum() {
        return carNum == null ? "" : carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
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
            jsonObj.put("carName", getCarName());
            jsonObj.put("carNum", getCarNum());
            jsonObj.put("carUrl", getCarUrl());
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
        dest.writeString(this.carName);
        dest.writeString(this.carNum);
        dest.writeString(this.carUrl);
        dest.writeString(this.toName);
    }

    protected PrivateCarMessage(Parcel in) {
        this.carName = in.readString();
        this.carNum = in.readString();
        this.carUrl = in.readString();
        this.toName = in.readString();
    }

    public static final Creator<PrivateCarMessage> CREATOR = new Creator<PrivateCarMessage>() {
        @Override
        public PrivateCarMessage createFromParcel(Parcel source) {
            return new PrivateCarMessage(source);
        }

        @Override
        public PrivateCarMessage[] newArray(int size) {
            return new PrivateCarMessage[0];
        }
    };

    /**
     * 赠送座驾消息
     * @param targetId
     * @param carNum
     * @param carName
     * @param picUrl
     * @param toUserName
     */
    public static void sendMessage(String targetId,String carNum,String carName,String picUrl,String toUserName){
        //rongExtension.getTargetId()
        PrivateCarMessage message=new PrivateCarMessage();
        message.setCarNum(carNum);
        message.setCarName(carName);
        message.setCarUrl(picUrl);
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

