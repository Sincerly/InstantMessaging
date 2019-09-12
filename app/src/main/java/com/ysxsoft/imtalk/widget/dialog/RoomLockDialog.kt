package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.room_lock_dialog.*

/**
 *Create By 胡
 *on 2019/9/11 0011
 */
class  RoomLockDialog(var mContext: Context):ABSDialog(mContext){
    override fun initView() {
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        tv_cancle.setOnClickListener {
            if (edClickListener != null) {
                dismiss()
                edClickListener!!.setData(ed_room_name.text.toString().trim())
            }
        }
        tv_ok.setOnClickListener {
            if (TextUtils.isEmpty(ed_room_name.text.toString().trim())) {
                ToastUtils.showToast(mContext, "房间密码不能为空")
                return@setOnClickListener
            }
            if (edClickListener != null) {
                dismiss()
                edClickListener!!.setData(ed_room_name.text.toString().trim())
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.room_lock_dialog
    }

    interface EdClickListener {
        fun setData(string: String)
    }

    private var edClickListener: EdClickListener? = null
    fun setEdClickListener(edClickListener: EdClickListener) {
        this.edClickListener = edClickListener
    }
}