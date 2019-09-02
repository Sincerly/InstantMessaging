package com.ysxsoft.imtalk.rong;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.ysxsoft.imtalk.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class ZBPlugin implements IPluginModule,IPluginRequestPermissionResultCallback {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.img_szb);
    }

    @Override
    public String obtainTitle(Context context) {
        return "送装扮";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    @Override
    public boolean onRequestPermissionResult(Fragment fragment, RongExtension rongExtension, int i, @NonNull String[] strings, @NonNull int[] ints) {
        return false;
    }
}
