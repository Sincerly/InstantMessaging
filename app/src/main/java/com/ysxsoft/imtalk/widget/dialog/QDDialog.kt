package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.QdSignListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.QDActivity
import com.ysxsoft.imtalk.view.SignRulesActivity
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.qd_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class QDDialog(var mContext: Context) : ABSDialog(mContext) {

    override fun initView() {
        tv_qd.setOnClickListener {
            dismiss()
            mContext.startActivity(Intent(mContext, QDActivity::class.java))
        }
        tv_rules.setOnClickListener {
            mContext.startActivity(Intent(mContext, SignRulesActivity::class.java))
        }
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .SignList(SpUtils.getSp(this@QDDialog.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QdSignListBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: QdSignListBean?) {
                        if (t!!.code == 0) {
                            tv_day.setText(t.data.sign_day)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    override fun getLayoutResId(): Int {
        return R.layout.qd_dialog_layout
    }

}