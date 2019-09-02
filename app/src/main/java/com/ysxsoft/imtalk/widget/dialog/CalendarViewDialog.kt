package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.calendarview_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/12 0012
 */
class CalendarViewDialog(mContext: Context):ABSDialog(mContext){
    override fun initView() {
        img_cancle.setOnClickListener {
            dismiss()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.calendarview_dialog_layout
    }
}