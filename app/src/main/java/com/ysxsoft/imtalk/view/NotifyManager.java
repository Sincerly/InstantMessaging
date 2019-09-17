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
    private List<String> data = new ArrayList<>();
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
            addView(data.get(0));
        }
    }

    /**
     * 关闭滚动
     */
    public void stop() {
        isRunning = false;
    }

    public void setData(List<String> items) {
        this.data.clear();
        this.data.addAll(items);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    public void addData(String item) {
        this.data.add(item);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    public void addView(String d) {
        View v = View.inflate(context, R.layout.view_gold_notifycation, null);
        TextView name = v.findViewById(R.id.name);
        TextView num = v.findViewById(R.id.num);
        TextView gold = v.findViewById(R.id.gold);
        name.setText("Sincerly");
        num.setText("2222");
        gold.setText("金币");

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
}
