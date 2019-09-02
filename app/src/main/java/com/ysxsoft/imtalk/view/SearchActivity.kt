package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.SearchAdapter
import com.ysxsoft.imtalk.bean.SearchBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.activity_bank_card_list.*
import kotlinx.android.synthetic.main.search_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/13 0013
 */
class SearchActivity:BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
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
        return R.layout.search_layout
    }
    var mDataAdapter:SearchAdapter?=null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler(this)
    var bean:SearchBean?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackVisibily()
        //search_item_layout
        initView()
    }

    private fun initView() {
        tv_title_right.setOnClickListener {
            if (TextUtils.isEmpty(ed_search.text.toString().trim())){
                showToastMessage("搜索内容不能为空")
                return@setOnClickListener
            }
            onRefresh()
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
         mDataAdapter = SearchAdapter(mContext)
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

        mLuRecyclerViewAdapter!!.setOnItemClickListener { view, position ->
            val uid = mDataAdapter!!.dataList.get(position).uid.toString()
            MyDataActivity.startMyDataActivity(mContext,uid,"2")
        }

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
    }

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler(activity: SearchActivity) : Handler() {
        var ref = WeakReference<SearchActivity>(activity);
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
                .roomSearch(ed_search.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<SearchBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: SearchBean?) {
                       if (t!!.code==0){
                           showData(t)
                           mDataAdapter!!.addAll(t.data)
                           if (mSwipeRefreshLayout!!.isRefreshing()) {
                               mSwipeRefreshLayout!!.setRefreshing(false)
                           }
                           mRecyclerView.refreshComplete(t.data.size)
                           notifyDataSetChanged()
                       }else{
                           mSwipeRefreshLayout!!.setRefreshing(false)
                       }
                    }

                    override fun onCompleted() {

                    }
                })
    }

    private fun showData(t: SearchBean) {
        bean=t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
}