package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.upper_wheat_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/17 0017
 * 上麦
 */
class UpperWheatDialog(var mContext: Context, room_id: String):ABSDialog(mContext){

    override fun initView() {
        img_cancle.setOnClickListener {
            dismiss()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.upper_wheat_dialog_layout
    }
}