package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.widget.FrameLayout
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.view.ReportActivity
import kotlinx.android.synthetic.main.report_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/9/19 0019
 */
class ReportDialog(var mContext: Context, var uid: String) : BottomSheetDialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.report_dialog_layout)
        super.onCreate(savedInstanceState)
        window!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent)

        initView()
    }

    private fun initView() {
        tv_black.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("uid", AuthManager.getInstance().currentUserId)
            map.put("black_uid", uid)
            val body = RetrofitUtil.createJsonRequest(map)
            NetWork.getService(ImpService::class.java)
                    .userAddBlack(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CommonBean> {
                        override fun onError(e: Throwable?) {
                        }

                        override fun onNext(t: CommonBean?) {
                            ToastUtils.showToast(mContext, t!!.msg)
                            if (t.code==0){
                                dismiss()
                            }
                        }

                        override fun onCompleted() {
                        }
                    })
        }
        tv_report.setOnClickListener {
            dismiss()
            val intent = Intent(mContext, ReportActivity::class.java)
            intent.putExtra("be_uid",uid)
            mContext.startActivity(intent)

        }
        tv_cancle.setOnClickListener {
            dismiss()
        }

    }
}