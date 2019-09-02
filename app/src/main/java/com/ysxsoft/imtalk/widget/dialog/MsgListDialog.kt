package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMwUserBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.msg_list_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Create By 胡
 * on 2019/8/7 0007
 */
class MsgListDialog : ABSDialog {
    constructor(mContext: Context, userId: String, roomId: String) : super(mContext) {
        requestData(userId, roomId)
    }

    private fun requestData(userId: String, roomId: String) {
        NetWork.getService(ImpService::class.java)
                .roomMwUser(userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RoomMwUserBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomMwUserBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(this@MsgListDialog.context,t.data.icon,img_head)
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID："+t.data.tt_id)
                            tv_familly.setText("所在家族："+t.data.fmy_name)
//                            tv_tuhao.setText(t.data.)
                            tv_mei.setText("魅 "+t.data.ml_level)
                            tv_zs.setText(t.data.user_level)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    override fun initView() {
        img_cancle.setOnClickListener {
            dismiss()
        }
        tv_bm.setOnClickListener {
            dismiss()
            if (onMsgListDialogListener != null) {
                onMsgListDialogListener!!.bMClick()
            }
        }

        tv_xm.setOnClickListener {
            dismiss()
            if (onMsgListDialogListener != null) {
                onMsgListDialogListener!!.xMClick()
            }
        }

        tv_js.setOnClickListener {
            dismiss()
            if (onMsgListDialogListener != null) {
                onMsgListDialogListener!!.jMClick()
            }
        }

        tv_btsm.setOnClickListener {
            dismiss()
            if (onMsgListDialogListener != null) {
                onMsgListDialogListener!!.bTMClick()
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.msg_list_dialog_layout
    }

    interface OnMsgListDialogListener {
        fun bMClick()
        fun xMClick()
        fun jMClick()
        fun bTMClick()
    }

    private var onMsgListDialogListener: OnMsgListDialogListener? = null
    fun setOnMsgListDialog(onMsgListDialogListener: OnMsgListDialogListener) {
        this.onMsgListDialogListener = onMsgListDialogListener
    }
}
