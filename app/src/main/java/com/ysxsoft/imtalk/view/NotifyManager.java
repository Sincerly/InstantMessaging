package com.ysxsoft.imtalk.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serenegiant.cameracommon.ViewHelper;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils;
import com.ysxsoft.imtalk.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class NotifyManager {
    private Activity context;
    private List<Data> data = new ArrayList<>();
    private boolean isRunning = false;

    public NotifyManager(Activity context) {
        this.context = context;
    }

    /**
     * 开启滚动
     */
    public void start() {
        isRunning = true;
        if (data.size() > 0) {
            Data d=data.get(0);
            addView(d.getNickName(),d.getGiftName(),d.getGoldNum());
        }
    }

    /**
     * 关闭滚动
     */
    public void stop() {
        isRunning = false;
    }

    public void setData(List<Data> items) {
        this.data.clear();
        this.data.addAll(items);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    public void addData(Data item) {
        this.data.add(item);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    /**
     * 添加一个通知
     * @param nickname 昵称
     * @param giftName 礼物名称
     * @param goldNum  金币数量
     */
    public void addView(String nickname,String giftName,String goldNum) {
        if(isPaused==true){
            Log.e("tag","暂停中...");
            return;
        }else{
            Log.e("tag","播放中...");
        }
        View v = View.inflate(context, R.layout.view_gold_notifycation, null);
        TextView name = v.findViewById(R.id.name);
        TextView num = v.findViewById(R.id.num);
        TextView gold = v.findViewById(R.id.gold);
        name.setText(nickname);
        num.setText(giftName);
        gold.setText(goldNum);

        FrameLayout rootView = context.findViewById(android.R.id.content);
        rootView.measure(0, 0);
        int halfWidth = AppUtil.INSTANCE.getScreenWidth(context) + rootView.getWidth() / 2;
        int maxWidth = AppUtil.INSTANCE.getScreenWidth(context) + rootView.getWidth()*3/2;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        v.setX(halfWidth);//首先设定在屏幕外边
        rootView.addView(v);

        ViewCompat.animate(v).translationXBy(-maxWidth)
                .setDuration(10000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        //动画结束
                        if (data.size() > 0) {
                            rootView.removeView(v);
                            data.remove(0);
                            if (data.size() > 0) {
                                stop();
                            } else {
                                start();
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                }).start();
    }

    public static class Data{
        private String nickName;
        private String giftName;
        private String goldNum;

        public String getNickName() {
            return nickName == null ? "" : nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getGiftName() {
            return giftName == null ? "" : giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGoldNum() {
            return goldNum == null ? "" : goldNum;
        }

        public void setGoldNum(String goldNum) {
            this.goldNum = goldNum;
        }
    }

    public boolean isPaused=false;

    public void pause(){
        isPaused=true;
    }

    public void resume(){
        isPaused=false;
    }
}
