package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.view.ComWebViewActivity

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class HelpPopuwindows : PopupWindow {
    constructor(mContext: Context, resLayout: Int, view: View) : super(mContext) {
        initView(mContext, resLayout, view)
    }

    private fun initView(mContext: Context, resLayout: Int, view: View) {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val content = LayoutInflater.from(mContext).inflate(resLayout, null, false)
        val tv_help = content.findViewById<TextView>(R.id.tv_help)
        tv_help.setOnClickListener {
            dismiss()
            ComWebViewActivity.starComWebViewActivity(mContext, "常见问题", "")
        }
        contentView = content
        this.showAsDropDown(view)


    }

}