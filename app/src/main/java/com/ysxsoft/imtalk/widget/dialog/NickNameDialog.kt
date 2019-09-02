package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.ToastUtils

import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.nick_name_dialog_layout.*

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
class NickNameDialog(var mContext: Context) : ABSDialog(mContext) {

    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            if (TextUtils.isEmpty(ed_nickName.text.toString().trim())){
                ToastUtils.showToast(mContext,"昵称输入不能为空")
                return@setOnClickListener
            }
            dismiss()
            if (nickNameClickListener!=null){
                nickNameClickListener!!.nickName(ed_nickName.text.toString().trim())
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.nick_name_dialog_layout
    }

    interface NickNameClickListener{
        fun nickName(name:String)
    }
    private var nickNameClickListener: NickNameClickListener?=null
    fun setNickNameClickListener(nickNameClickListener: NickNameClickListener){
        this.nickNameClickListener=nickNameClickListener
    }
}
