package com.ysxsoft.imtalk.view;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.RoomPublicGiftMessageBean;
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils;
import com.ysxsoft.imtalk.utils.AppUtil;
import com.ysxsoft.imtalk.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class GiftNotifyManager {
    private Activity context;
    private List<RoomPublicGiftMessageBean> data = new ArrayList<>();
    private boolean isRunning = false;

    public GiftNotifyManager(Activity context) {
        this.context = context;
    }

    /**
     * 开启滚动
     */
    public void start() {
        isRunning = true;
        if (data.size() > 0) {
            RoomPublicGiftMessageBean d=data.get(0);
            addView(d);
        }
    }

    /**
     * 关闭滚动
     */
    public void stop() {
        isRunning = false;
    }

    public void setData(List<RoomPublicGiftMessageBean> items) {
        this.data.clear();
        this.data.addAll(items);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    public void addData(RoomPublicGiftMessageBean item) {
        this.data.add(item);
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    /**
     * 添加一个通知
     */
    public void addView(RoomPublicGiftMessageBean d) {
        View v = View.inflate(context, R.layout.view_gift_notifycation, null);
        CircleImageView sicon=v.findViewById(R.id.sicon);
        CircleImageView ticon=v.findViewById(R.id.ticon);
        CircleImageView gpic=v.findViewById(R.id.gpic);
        TextView sname = v.findViewById(R.id.sname);
        TextView tname = v.findViewById(R.id.tname);
        TextView num = v.findViewById(R.id.num);
        Glide.with(context).load(d.getSendIcon()).into(sicon);
        Glide.with(context).load(d.getGiftPic()).into(gpic);
        Glide.with(context).load(d.getSlIcon()).into(ticon);
        sname.setText(d.getSendName());
        tname.setText(d.getSlName());
        num.setText("x"+d.getGiftNums());

        FrameLayout rootView = context.findViewById(android.R.id.content);
        rootView.measure(0, 0);
        int halfWidth = AppUtil.INSTANCE.getScreenWidth(context) + rootView.getWidth() / 2;
        int maxWidth = AppUtil.INSTANCE.getScreenWidth(context) + rootView.getWidth()*3/2;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin= DisplayUtils.dp2px(context,48);
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
