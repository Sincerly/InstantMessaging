package com.ysxsoft.imtalk.chatroom.utils;

import android.app.Application;
import android.content.Context;

/**
 * Create By 胡
 * on 2019/8/5 0005
 */
public class MyApplication extends Application {

    public static Context mcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext=this;
    }
}
