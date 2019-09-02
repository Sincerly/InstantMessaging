package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.room_name_dialog.*

/**
 *Create By 胡
 *on 2019/7/13 0013
 */
class RoomNameDialog(var mContext: Context) : ABSDialog(mContext) {
    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            if (TextUtils.isEmpty(ed_room_name.text.toString().trim())) {
                ToastUtils.showToast(mContext, "房间名不能为空")
                return@setOnClickListener
            }
            if (edClickListener != null) {
                dismiss()
                edClickListener!!.setData(ed_room_name.text.toString().trim())
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.room_name_dialog
    }

    interface EdClickListener {
        fun setData(string: String)
    }

    private var edClickListener: EdClickListener? = null
    fun setEdClickListener(edClickListener: EdClickListener) {
        this.edClickListener = edClickListener
    }
}