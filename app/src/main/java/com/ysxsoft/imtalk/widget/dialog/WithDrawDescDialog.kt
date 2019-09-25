package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.with_draw_dialog.*

/**
 *Create By 胡
 *on 2019/9/25 0025
 */
class WithDrawDescDialog(mContext: Context,var desc:String):ABSDialog(mContext){
    override fun initView() {
        tv_desc.setText(desc)
        tv_ok.setOnClickListener {
            dismiss()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.with_draw_dialog
    }
}