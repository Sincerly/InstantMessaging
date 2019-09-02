package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.share_friend_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/26 0026
 */
class ShareFriendDialog:ABSDialog{

    constructor(mContext: Context):super(mContext){
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    override fun initView() {
        tv_myself.setOnClickListener {
            dismiss()
            if (shareListener!=null){
                shareListener!!.myself()
            }
        }
        tv_wechat.setOnClickListener {
            dismiss()
            if (shareListener!=null){
                shareListener!!.Wechat()
            }
        }
        tv_pyq.setOnClickListener {
            dismiss()
            if (shareListener!=null){
                shareListener!!.pyq()
            }
        }
        tv_qq_zone.setOnClickListener {
            dismiss()
            if (shareListener!=null){
                shareListener!!.qqZone()
            }
        }

        tv_qq.setOnClickListener {
            dismiss()
            if (shareListener!=null){
                shareListener!!.QQ()
            }
        }
        tv_cancle.setOnClickListener { dismiss() }
    }

    override fun getLayoutResId(): Int {
        return R.layout.share_friend_dialog_layout
    }

    interface ShareListener{
        fun myself()
        fun Wechat()
        fun pyq()
        fun qqZone()
        fun QQ()
    }
    private var shareListener: ShareListener?=null
    fun setShareListener(shareListener: ShareListener){
        this.shareListener=shareListener
    }

}