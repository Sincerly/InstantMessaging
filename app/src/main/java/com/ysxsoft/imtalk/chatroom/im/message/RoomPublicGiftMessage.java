package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;
import android.util.Log;

import com.ysxsoft.imtalk.chatroom.utils.log.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;


/**
 * 全局礼物消息
 */
@MessageTag(value = "Stu:score", flag = MessageTag.NONE)
public class RoomPublicGiftMessage extends MessageContent {
    private final String TAG = RoomPublicGiftMessage.class.getSimpleName();
    //送礼物
    private String sendName;
    private String sendIcon;
    private String slName;
    private String slIcon;
    private String giftPic;
    private String giftNums;
    //砸金蛋
    private String nickname;//昵称
    private String sgName;//礼物名称
    private String goldNums;//金币数量

    public RoomPublicGiftMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            setSendName(jsonObj.optString("send_name"));
            setSendIcon(jsonObj.optString("send_icon"));
            setSlName(jsonObj.optString("sl_name"));
            setSlIcon(jsonObj.optString("sl_icon"));
            setGiftPic(jsonObj.optString("gift_pic"));
            setGiftNums(jsonObj.optString("gift_nums"));
            //砸金蛋
            setNickname(jsonObj.optString("nickname"));
            setSgName(jsonObj.optString("sg_name"));
            setGoldNums(jsonObj.optString("gold_nums"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSgName() {
        return sgName == null ? "" : sgName;
    }

    public void setSgName(String sgName) {
        this.sgName = sgName;
    }

    public String getGoldNums() {
        return goldNums == null ? "" : goldNums;
    }

    public void setGoldNums(String goldNums) {
        this.goldNums = goldNums;
    }

    public String getSendName() {
        return sendName == null ? "" : sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendIcon() {
        return sendIcon == null ? "" : sendIcon;
    }

    public void setSendIcon(String sendIcon) {
        this.sendIcon = sendIcon;
    }

    public String getSlName() {
        return slName == null ? "" : slName;
    }

    public void setSlName(String slName) {
        this.slName = slName;
    }

    public String getSlIcon() {
        return slIcon == null ? "" : slIcon;
    }

    public void setSlIcon(String slIcon) {
        this.slIcon = slIcon;
    }

    public String getGiftPic() {
        return giftPic == null ? "" : giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getGiftNums() {
        return giftNums == null ? "" : giftNums;
    }

    public void setGiftNums(String giftNums) {
        this.giftNums = giftNums;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("send_name", sendName);
            jsonObj.put("send_icon", sendIcon);
            jsonObj.put("sl_name",slName);
            jsonObj.put("sl_icon",slIcon);
            jsonObj.put("gift_pic", giftPic);
            jsonObj.put("gift_nums", giftNums);

            jsonObj.put("nickname", nickname);
            jsonObj.put("sg_name", sgName);
            jsonObj.put("gold_nums", goldNums);
            byte[] bytes = jsonObj.toString().getBytes("UTF-8");
            return bytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RoomPublicGiftMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sendName);
        dest.writeString(this.sendIcon);
        dest.writeString(this.slName);
        dest.writeString(this.slIcon);
        dest.writeString(this.giftPic);
        dest.writeString(this.giftNums);
        dest.writeString(this.nickname);
        dest.writeString(this.sgName);
        dest.writeString(this.goldNums);
    }

    protected RoomPublicGiftMessage(Parcel in) {
        this.sendName = in.readString();
        this.sendIcon = in.readString();
        this.slName = in.readString();
        this.slIcon = in.readString();
        this.giftPic = in.readString();
        this.giftNums = in.readString();
        this.nickname=in.readString();
        this.sgName=in.readString();
        this.goldNums=in.readString();
    }

    public static final Creator<RoomPublicGiftMessage> CREATOR = new Creator<RoomPublicGiftMessage>() {
        @Override
        public RoomPublicGiftMessage createFromParcel(Parcel source) {
            return new RoomPublicGiftMessage(source);
        }

        @Override
        public RoomPublicGiftMessage[] newArray(int size) {
            return new RoomPublicGiftMessage[size];
        }
    };
}
