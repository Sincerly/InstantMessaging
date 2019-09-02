package com.ysxsoft.imtalk.appservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.utils.AppUtil;
import com.ysxsoft.imtalk.widget.CircleImageView;

/**
 * Create By 胡
 * on 2019/7/26 0026
 */
public class MyService extends Service {
    // 手机窗体布局的管理者
    private WindowManager mWindowManager;
    // 手机窗体的布局
    private WindowManager.LayoutParams mParams;
    // 展示头像的自定义布局
    private View mToastRocketView;
    // 手机窗体的宽度
    private int mWindowWidth;
    // 手机窗体的高度
    private int mWindowHeight;
    // 展示头像的ImageView
    private CircleImageView mRocketImage;

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 获取手机屏幕的宽高值
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        mParams = new WindowManager.LayoutParams();
        // 服务启动，打开自定义Toast的控件
        showRocketView();
        // 拖拽头像到任意位置
        dragRocket();
    }

    private void showRocketView() {
        // 自定义Toast
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // 修改完右下角对齐
        mParams.gravity = Gravity.RIGHT + Gravity.BOTTOM;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        mToastRocketView = View.inflate(this, R.layout.floatwindow_layout, null);
        // 窗体布局中加入自定义的展示头像的View
        mWindowManager.addView(mToastRocketView, mParams);
        mRocketImage = (CircleImageView) mToastRocketView.findViewById(R.id.img_head);

    }


    private void dragRocket() {
        mToastRocketView.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        // 两个方向上所移动的距离值
                        int disX = moveX - startX;
                        int disY = moveY - startY;
                        mParams.x = mParams.x - disX;
                        mParams.y = mParams.y - disY;

                        if (mParams.x < 0) {
                            mParams.x = 0;
                        }

                        if (mParams.y < 0) {
                            mParams.y = 0;
                        }
//                        if (mParams.x > mWindowManager.getDefaultDisplay().getWidth() - v.getWidth()) {
//                            mParams.x = mWindowManager.getDefaultDisplay().getWidth();
//                        }
//
//                        if (mParams.y > mWindowManager.getDefaultDisplay().getHeight() - 21 - v.getHeight()) {
//                            mParams.y = mWindowManager.getDefaultDisplay().getHeight()- 21;
//                        }

                        // 更新头像的坐标位置X和Y值
                        mWindowManager.updateViewLayout(mToastRocketView, mParams);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mParams.x = -mWindowWidth;
                        mWindowManager.updateViewLayout(mToastRocketView, mParams);
                        break;
                }
                return false;
            }
        });


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null && mToastRocketView != null) {
            mWindowManager.removeView(mToastRocketView);
        }
    }
}
