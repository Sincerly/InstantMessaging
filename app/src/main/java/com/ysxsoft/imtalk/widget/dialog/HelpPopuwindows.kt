package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ScaleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.SystemHelpBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.view.ComWebViewActivity
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class HelpPopuwindows : PopupWindow {
    constructor(mContext: Context, resLayout: Int, view: View) : super(mContext) {
        initView(mContext, resLayout, view)
    }

    private fun initView(mContext: Context, resLayout: Int, view: View) {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val content = LayoutInflater.from(mContext).inflate(resLayout, null, false)
        val tv_help = content.findViewById<TextView>(R.id.tv_help)
        tv_help.setOnClickListener {
            dismiss()
            NetWork.getService(ImpService::class.java)
                    .system_help()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :Observer<SystemHelpBean>{
                        override fun onError(e: Throwable?) {
                        }

                        override fun onNext(t: SystemHelpBean?) {
                            if (t!!.code==0){
                                ComWebViewActivity.starComWebViewActivity(mContext, "帮助", t.data)
                            }
                        }

                        override fun onCompleted() {
                        }

                    })

        }
        contentView = content
        this.showAsDropDown(view)


    }

}