package com.ysxsoft.imtalk.widget.dialog;


import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.widget.ABSDialog;

/**
 * Create By 胡
 * on 2019/6/5 0005
 */
public class UpdataDialog extends ABSDialog {

    public UpdataDialog(@NonNull Context context) {
        super(context);
        //设置点击屏幕不消失
        this.setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        this.setCancelable(false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.update_bar_layout;
    }
}
