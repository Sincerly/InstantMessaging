package com.ysxsoft.imtalk.chatroom.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class MicPositionsBean implements Parcelable {
    private String mw_id;
    private String room_id;
    private String uid; // 当前麦位上的人员 id
    private String icon;
    private String sex;
    private int sort;
    private String nickname;
    private String is_room;
    private String is_admin;
    private String is_block;
    private String is_wheat;
    private String is_lock_wheat;
    private String add_time;
    private String gifts;
    private String is_oc_wheat;

    public String getIs_oc_wheat() {
        return is_oc_wheat;
    }

    public void setIs_oc_wheat(String is_oc_wheat) {
        this.is_oc_wheat = is_oc_wheat;
    }

    protected MicPositionsBean(Parcel in) {
        mw_id = in.readString();
        room_id = in.readString();
        uid = in.readString();
        icon = in.readString();
        sex = in.readString();
        sort = in.readInt();
        nickname = in.readString();
        is_room = in.readString();
        is_admin = in.readString();
        is_block = in.readString();
        is_wheat = in.readString();
        is_lock_wheat = in.readString();
        add_time = in.readString();
        gifts = in.readString();
        is_oc_wheat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mw_id);
        dest.writeString(room_id);
        dest.writeString(uid);
        dest.writeString(icon);
        dest.writeString(sex);
        dest.writeInt(sort);
        dest.writeString(nickname);
        dest.writeString(is_room);
        dest.writeString(is_admin);
        dest.writeString(is_block);
        dest.writeString(is_wheat);
        dest.writeString(is_lock_wheat);
        dest.writeString(add_time);
        dest.writeString(gifts);
        dest.writeString(is_oc_wheat);
    }
    public MicPositionsBean() {
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MicPositionsBean> CREATOR = new Creator<MicPositionsBean>() {
        @Override
        public MicPositionsBean createFromParcel(Parcel in) {
            return new MicPositionsBean(in);
        }

        @Override
        public MicPositionsBean[] newArray(int size) {
            return new MicPositionsBean[size];
        }
    };

    public String getMw_id() {
        return mw_id;
    }

    public void setMw_id(String mw_id) {
        this.mw_id = mw_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIs_room() {
        return is_room;
    }

    public void setIs_room(String is_room) {
        this.is_room = is_room;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getIs_block() {
        return is_block;
    }

    public void setIs_block(String is_block) {
        this.is_block = is_block;
    }

    public String getIs_wheat() {
        return is_wheat;
    }

    public void setIs_wheat(String is_wheat) {
        this.is_wheat = is_wheat;
    }

    public String getIs_lock_wheat() {
        return is_lock_wheat;
    }

    public void setIs_lock_wheat(String is_lock_wheat) {
        this.is_lock_wheat = is_lock_wheat;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getGifts() {
        return gifts;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }

    public static MicPositionsBean parseJsonToMicPositionInfo(JSONObject jsonObject) {
        MicPositionsBean micPositionInfo = new MicPositionsBean();
        micPositionInfo.setMw_id(jsonObject.optString("mw_id"));
        micPositionInfo.setRoom_id(jsonObject.optString("room_id"));
        micPositionInfo.setUid(jsonObject.optString("uid"));
        micPositionInfo.setIcon(jsonObject.optString("icon"));
        micPositionInfo.setSex(jsonObject.optString("sex"));
        micPositionInfo.setSort(jsonObject.optInt("sort"));
        micPositionInfo.setNickname(jsonObject.optString("nickname"));
        micPositionInfo.setIs_room(jsonObject.optString("is_room"));
        micPositionInfo.setIs_admin(jsonObject.optString("is_admin"));
        micPositionInfo.setIs_block(jsonObject.optString("is_block"));
        micPositionInfo.setIs_wheat(jsonObject.optString("is_wheat"));
        micPositionInfo.setIs_lock_wheat(jsonObject.optString("is_lock_wheat"));
        micPositionInfo.setAdd_time(jsonObject.optString("add_time"));
        micPositionInfo.setGifts(jsonObject.optString("gifts"));
        micPositionInfo.setIs_oc_wheat(jsonObject.optString("is_oc_wheat"));
        return micPositionInfo;
    }
}
