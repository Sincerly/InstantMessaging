package com.ysxsoft.imtalk.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.MoneyZsAdapter
import com.ysxsoft.imtalk.adapter.WithDrawRecodeAdapter
import com.ysxsoft.imtalk.bean.RecordDetailBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.ExchangeZSActivity
import com.ysxsoft.imtalk.view.WithDrawRecodeActivity
import com.ysxsoft.imtalk.view.ZsWithDrawActivity
import kotlinx.android.synthetic.main.fm_money_zs.*
import kotlinx.android.synthetic.main.room_tag_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * 我的钱包-钻石
 */
class MoneyZsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

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
        return R.layout.fm_money_zs
    }

    var mDataAdapter: MoneyZsAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var page = 1
    var type = 0
    var bean:RecordDetailBean?=null
    override fun onResume() {
        super.onResume()
        //fm_money_zs_item
        initView()
    }

    private fun initView() {
        tv_all.isSelected = true
        tv_dzs.setOnClickListener {
            tv_dzs.isSelected = true
            tv_tx.isSelected = false
//            if (bean==null){
//                showToastMessage("无法兑换")
//                return@setOnClickListener
//            }
//            ExchangeZSActivity.starExchangeZSActivity(mContext,bean!!.data.diamond)
            startActivity(ExchangeZSActivity::class.java)
        }

        tv_tx.setOnClickListener {
            tv_tx.isSelected = true
            tv_dzs.isSelected = false
            startActivity(ZsWithDrawActivity::class.java)
        }

        tv_all.setOnClickListener {
            tv_all.isSelected = true
            tv_down.isSelected = false
            tv_up.isSelected = false
            type = 0
            onRefresh()
        }
        tv_down.setOnClickListener {
            tv_all.isSelected = false
            tv_down.isSelected = true
            tv_up.isSelected = false
            type = 2
            onRefresh()
        }

        tv_up.setOnClickListener {
            tv_all.isSelected = false
            tv_down.isSelected = false
            tv_up.isSelected = true
            type = 1
            onRefresh()
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = MoneyZsAdapter(mContext)

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
        onRefresh()
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
                    mRecyclerView.refreshComplete(bean!!.data.recordList.size)
                    notifyDataSetChanged()
                } else {
                    mRecyclerView.setOnNetWorkErrorListener(OnNetWorkErrorListener {
                        mRecyclerView.refreshComplete(bean!!.data.recordList.size)
                        notifyDataSetChanged()
                        requestData()
                    })
                }
            }
        }
    }

    private fun getData() {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("type", "1")
        map.put("page", page.toString())
        if (type != 0) {
            map.put("status", type.toString())
        }
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .record_detail(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<RecordDetailBean> {
                    override fun call(t: RecordDetailBean?) {
                        if (t!!.code == 0) {
                            tv_moeny.setText(t.data.diamond)
                            showData(t)
                            mDataAdapter!!.addAll(t.data.recordList)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.recordList.size)
                            notifyDataSetChanged()
                        }else{
                            mSwipeRefreshLayout!!.setRefreshing(false)
                        }
                    }
                })
    }

    private fun showData(t: RecordDetailBean) {
        bean=t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
}