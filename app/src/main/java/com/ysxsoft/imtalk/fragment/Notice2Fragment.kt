package com.ysxsoft.imtalk.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.Notice2FragmentAdapter
import com.ysxsoft.imtalk.bean.SysBean
import com.ysxsoft.imtalk.bean.SysDetialBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.BankCardListActivity
import com.ysxsoft.imtalk.view.ComWebViewActivity
import kotlinx.android.synthetic.main.notice2_fragment_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class Notice2Fragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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

    override fun getLayoutResId(): Int {
        return R.layout.notice2_fragment_layout
    }

    var mDataAdapter: Notice2FragmentAdapter? = null

    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var page = 1
    var bean:SysBean?=null
    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = Notice2FragmentAdapter(mContext)
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
            val sid = mDataAdapter!!.dataList.get(position).sid.toString()
            clickData(sid)
        }

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
        onRefresh()
    }

    private fun clickData(sid: String) {
        val map = HashMap<String, String>()
        map.put("uid",SpUtils.getSp(mContext,"uid"))
        map.put("sid",sid)
        val body = RetrofitUtil.createJsonRequest(map)

        NetWork.getService(ImpService::class.java)
                .sys_detail(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<SysDetialBean>{
                    override fun onError(e: Throwable?) {
                        Log.d(this@Notice2Fragment.javaClass.name,e!!.message.toString())
                    }

                    override fun onNext(t: SysDetialBean?) {
                        if (t!!.code==0){
                            ComWebViewActivity.starComWebViewActivity(mContext,t.data.sys_title,t.data.content)
                        }
                    }
                    override fun onCompleted() {

                    }
                })


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
        val map = HashMap<String, String>()
        map.put("uid",SpUtils.getSp(mContext,"uid"))
        map.put("page",page.toString())
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .sys_list(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<SysBean>{
                    override fun onError(e: Throwable?) {
                        Log.d(this@Notice2Fragment.javaClass.name,e!!.message.toString())
                    }

                    override fun onNext(t: SysBean?) {
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

    private fun showData(t: SysBean) {
        bean=t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

}