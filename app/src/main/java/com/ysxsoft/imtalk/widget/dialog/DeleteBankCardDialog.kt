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
 * 删除银行卡
 */
class DeleteBankCardDialog(var mContext: Context) : ABSDialog(mContext) {

    override fun initView() {
        tv_dec.setText("要删除此银行卡吗？")
        tv_cancle.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            dismiss()
            if (deleteBankCardDialog != null) {
                deleteBankCardDialog!!.deleteClick()
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.login_out_dialog_layout
    }

    interface DeleteClickListener {
        fun deleteClick()
    }

     var deleteBankCardDialog: DeleteClickListener? = null
     fun setDeleteClickListener(deleteBankCardDialog: DeleteClickListener) {
        this.deleteBankCardDialog = deleteBankCardDialog
    }
}