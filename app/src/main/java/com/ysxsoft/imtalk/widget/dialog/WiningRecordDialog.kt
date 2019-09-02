package com.ysxsoft.imtalk.widget.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.WiningRecordAdapter
import com.ysxsoft.imtalk.bean.AwardRecodeBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.wining_record_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/17 0017
 * 我的中奖纪录
 */
class WiningRecordDialog : ABSDialog, SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {
        page = 1
        mSwipeRefreshLayout.isRefreshing = true
        mRecyclerView.setRefreshing(true)//同时调用LuRecyclerView的setRefreshing方法
        requestData()
    }

    private fun requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(this.context)!!) {
            mHandler.sendEmptyMessage(-1)
        } else {
            mHandler.sendEmptyMessage(-3)
        }
    }

    var mDataAdapter: WiningRecordAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    var page = 1
    val mHandler = PreviewHandler()
    var bean: AwardRecodeBean? = null

    constructor(mContext: Context) : super(mContext) {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = WiningRecordAdapter(mContext)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.adapter = mLuRecyclerViewAdapter
        val divider = LuDividerDecoration.Builder(mContext, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.transparent)
                .build()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(divider)
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
        onRefresh()
    }

    override fun initView() {
        tv_activity_rules.setText("我的中奖纪录")
        img_close.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler() : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                -1 -> {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter!!.clear()
                    }
                    getData()
                }
                -3 -> if (mSwipeRefreshLayout!!.isRefreshing()) {
                    mSwipeRefreshLayout!!.setRefreshing(false)
                    mRecyclerView.refreshComplete(bean!!.data.size)
                    notifyDataSetChanged()
                } else {
                    mRecyclerView.setOnNetWorkErrorListener(OnNetWorkErrorListener {
                        mRecyclerView.refreshComplete(bean!!.data.size)
                        notifyDataSetChanged()
                        requestData()
                    })
                }
            }
        }
    }

    private fun getData() {
        NetWork.getService(ImpService::class.java)
                .awardRecode(SpUtils.getSp(this.context, "uid"), page.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AwardRecodeBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: AwardRecodeBean?) {
                        if ("0".equals(t!!.code)) {
                            showData(t)
                            mDataAdapter!!.addAll(t.data)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.size)
                            notifyDataSetChanged()
                        }
                    }

                    override fun onCompleted() {
                    }

                })

    }

    private fun showData(t: AwardRecodeBean) {
        bean = t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    override fun getLayoutResId(): Int {
        return R.layout.wining_record_dialog_layout
    }
}