package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.InviteAdapter
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.MyInvitationRecodeBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_invite_list.*
import kotlinx.android.synthetic.main.title_layout2.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

class InviteListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        page = 1
        mSwipeRefreshLayout.isRefreshing = true
        mRecyclerView.setRefreshing(true)//同时调用LuRecyclerView的setRefreshing方法
        requestData()
    }

    private fun requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(mContext)!!) {
            mHandler.sendEmptyMessage(-1)
        } else {
            mHandler.sendEmptyMessage(-3)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_invite_list
    }

    var mDataAdapter: InviteAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler(this)
    var page = 1
    var bean: MyInvitationRecodeBean?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initStatusBar(topView)
        initView()
    }

    private fun initView() {
        setBackVisibily()
        setTitle("我的邀请列表")

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = InviteAdapter(mContext);
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

        mRecyclerView.setOnLoadMoreListener {
            if (bean != null) {
                if (page < bean!!.getLast_page()) {
                    page++
                    requestData()
                } else {
                    //the end
                    mRecyclerView.setNoMore(true)
                }
            }
        }
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
    }

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler(activity: InviteListActivity) : Handler() {
        var ref = WeakReference<InviteListActivity>(activity);
        override fun handleMessage(msg: Message?) {
            var activity = ref.get()
            if (activity == null || activity.isFinishing()) {
                return
            }
            when (msg!!.what) {
                -1 -> {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter!!.clear()
                    }
                    getData()
                }
                -3 -> if (mSwipeRefreshLayout!!.isRefreshing()) {
                    mSwipeRefreshLayout!!.setRefreshing(false)
                    mRecyclerView.refreshComplete(bean!!.data.user_info.size)
                    notifyDataSetChanged()
                } else {
                    mRecyclerView.setOnNetWorkErrorListener(OnNetWorkErrorListener {
                        mRecyclerView.refreshComplete(bean!!.data.user_info.size)
                        notifyDataSetChanged()
                        requestData()
                    })
                }
            }
        }
    }

    private fun getData() {
        NetWork.getService(ImpService::class.java)
                .myInvitationRecode(SpUtils.getSp(mContext,"uid"),page.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<MyInvitationRecodeBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: MyInvitationRecodeBean?) {
                        if (t!!.code==0) {
                            showData(t)
                            mDataAdapter!!.addAll(t.data.user_info)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.user_info.size)
                            notifyDataSetChanged()
                        }else{
                            mSwipeRefreshLayout!!.setRefreshing(false)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    private fun showData(data: MyInvitationRecodeBean?) {
        bean=data
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }
}
