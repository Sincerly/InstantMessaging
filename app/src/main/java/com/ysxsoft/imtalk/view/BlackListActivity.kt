package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.BlackListAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.UserBlackListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_black_list.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class BlackListActivity : BaseActivity(){

    override fun getLayout(): Int {
        return R.layout.activity_black_list
    }

    var mAdapter : BlackListAdapter ? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        setBackVisibily()
        setTitle("黑名单")
        initView()
        initData()
    }

    private fun initData() {
        NetWork.getService(ImpService::class.java)
                .userBlackList(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<UserBlackListBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserBlackListBean?) {
                        if (t!!.code==0){
                            val data = t.data
                            recyclerView.adapter = mAdapter
                            mAdapter!!.addAll(data)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun initView() {
        mAdapter = BlackListAdapter(mContext)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter!!.setOnDelListener(object :BlackListAdapter.OnDelListener{
            override fun onDelClick(position: Int) {
                val black_uid = mAdapter!!.dataList.get(position).black_uid
                val black_id = mAdapter!!.dataList.get(position).id
                Deldata(black_uid,black_id)
            }
        })
    }

    private fun Deldata(black_uid: String?, black_id: Int) {
        NetWork.getService(ImpService::class.java)
                .userRemoveBlack(SpUtils.getSp(mContext,"uid"),black_uid!!,black_id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })



    }
}
