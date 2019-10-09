package com.ysxsoft.imtalk.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.HelpAdapter
import com.ysxsoft.imtalk.bean.HelpBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class HelpActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_help;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        setBackVisibily()
        setTitle("帮助")

        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .BaseSetHelp("4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<HelpBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: HelpBean?) {
                        if (t!!.code==0){
                            val adapter = HelpAdapter(mContext)
                            recyclerView.layoutManager=LinearLayoutManager(mContext)
                            recyclerView.adapter=adapter
                            adapter.addAll(t.data)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
