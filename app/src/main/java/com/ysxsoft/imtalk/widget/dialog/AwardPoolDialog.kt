package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.AwardPoolAdapter
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.award_pool_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class AwardPoolDialog:ABSDialog{

    constructor(mContext: Context) : super(mContext) {
        requestData(mContext)
    }

    private fun requestData(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .awardList(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<AwardListDataBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: AwardListDataBean?) {
                       if (t!!.code==0){
                           val adapter = AwardPoolAdapter(mContext)
                           recyclerView.layoutManager = LinearLayoutManager(mContext)
                           recyclerView.adapter = adapter
                           adapter.addAll(t.data.awardList)
                       }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    override fun initView() {
        img_close.setOnClickListener {
            dismiss()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.award_pool_dialog_layout
    }
}