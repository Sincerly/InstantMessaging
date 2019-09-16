package com.ysxsoft.imtalk.bean;

public class RoomPublicGiftMessageBean {
    private String sendName;
    private String sendIcon;
    private String slName;
    private String slIcon;
    private String giftPic;
    private String giftNums;

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
}
