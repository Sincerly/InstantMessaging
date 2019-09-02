package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.ActivityPageManager
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.LoginActivity
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.login_out_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/12 0012
 */
class LoginOutDialog(var mContext: Context):ABSDialog(mContext){

    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            dismiss()
            var instance = ActivityPageManager.getInstance();
            instance!!.finishAllActivity();
            SpUtils.deleteSp(mContext)
            mContext.startActivity(Intent(mContext,LoginActivity::class.java))
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.login_out_dialog_layout
    }
}