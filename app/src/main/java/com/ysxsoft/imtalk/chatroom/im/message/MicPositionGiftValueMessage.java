package com.ysxsoft.imtalk.chatroom.im.message;

import android.os.Parcel;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.utils.log.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 房间礼物值
 */
@MessageTag(value = "SM:MPGVMsg", flag = MessageTag.NONE)
public class MicPositionGiftValueMessage extends MessageContent {
    private final static String TAG = MicPositionGiftValueMessage.class.getSimpleName();
    private int cmd;//  0关闭礼物值  1显示礼物值
    private String houseOwnerValue;// 房主礼物值
    private List<MicPositionsBean> micPositions;

    public MicPositionGiftValueMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            SLog.e(TAG, "UnsupportedEncodingException ", e);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            setCmd(jsonObj.optInt("cmd"));
            setHouseOwnerValue(jsonObj.optString("houseOwnerValue"));
            if (jsonObj.has("micPositions")){
                JSONArray positions = jsonObj.optJSONArray("micPositions");
                List<MicPositionsBean> infoList = new ArrayList<>();
                if (positions != null && positions.length() > 0) {
                    for (int i = 0; i < positions.length(); i++) {
                        infoList.add(MicPositionsBean.parseJsonToMicPositionInfo(positions.getJSONObject(i)));
                    }
                }
                setMicPositions(infoList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }


    public String getHouseOwnerValue() {
        return houseOwnerValue;
    }

    public void setHouseOwnerValue(String houseOwnerValue) {
        this.houseOwnerValue = houseOwnerValue;
    }

    public List<MicPositionsBean> getMicPositions() {
        return micPositions;
    }

    public void setMicPositions(List<MicPositionsBean> micPositions) {
        this.micPositions = micPositions;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("cmd", getCmd());
            jsonObj.put("houseOwnerValue", getHouseOwnerValue());
            String json=JSON.toJSONString(getMicPositions());

            com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(json);
            JSONArray array=new JSONArray(jsonArray.toJSONString());
            jsonObj.put("micPositions", array);
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
        dest.writeInt(this.cmd);
        dest.writeString(this.houseOwnerValue);
        dest.writeTypedList(this.micPositions);
    }

    public MicPositionGiftValueMessage() {
    }

    protected MicPositionGiftValueMessage(Parcel in) {
        this.cmd = in.readInt();
        this.houseOwnerValue = in.readString();
        this.micPositions =in.createTypedArrayList(MicPositionsBean.CREATOR);
    }

    public static final Creator<MicPositionGiftValueMessage> CREATOR = new Creator<MicPositionGiftValueMessage>() {
        @Override
        public MicPositionGiftValueMessage createFromParcel(Parcel source) {
            return new MicPositionGiftValueMessage(source);
        }

        @Override
        public MicPositionGiftValueMessage[] newArray(int size) {
            return new MicPositionGiftValueMessage[0];
        }
    };
}
