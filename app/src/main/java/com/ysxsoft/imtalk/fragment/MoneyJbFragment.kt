package com.ysxsoft.imtalk.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.MoneyJbAdapter
import com.ysxsoft.imtalk.bean.JbRecordDetailBean
import com.ysxsoft.imtalk.bean.RecordDetailBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.ExchangeJBActivity
import com.ysxsoft.imtalk.view.JbWithDrawActivity
import kotlinx.android.synthetic.main.fm_money_jb.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * 我的钱包-金币
 */
class MoneyJbFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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
        return R.layout.fm_money_jb
    }

    var mDataAdapter: MoneyJbAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var page = 1
    var type = 0
    var bean: JbRecordDetailBean? = null
    override fun onResume() {
        super.onResume()
        //fm_money_zs_item
        initView()
    }

    private fun initView() {

        tv_all.isSelected = true
        tv_djb.setOnClickListener {
            tv_djb.isSelected = true
            tv_cz.isSelected = false
            if (bean==null){
                showToastMessage("无法兑换")
                return@setOnClickListener
            }
            ExchangeJBActivity.starExchangeJBActivity(mContext,bean!!.data.moneys)
        }

        tv_cz.setOnClickListener {
            tv_cz.isSelected = true
            tv_djb.isSelected = false
            startActivity(JbWithDrawActivity::class.java)
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
            type = 2
            onRefresh()
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }

        mDataAdapter = MoneyJbAdapter(mContext)
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
        map.put("type", "2")
        map.put("page", page.toString())
        if (type != 0) {
            map.put("status", type.toString())
        }
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .record_detail1(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<JbRecordDetailBean> {
                    override fun call(t: JbRecordDetailBean?) {
                        if (t!!.code == 0) {
                            tv_moeny.setText(t.data.moneys)
                            showData(t)
                            mDataAdapter!!.addAll(t.data.recordList)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.recordList.size)
                            notifyDataSetChanged()
                        } else {
                            mSwipeRefreshLayout!!.setRefreshing(false)
                        }
                    }
                })
    }

    private fun showData(t: JbRecordDetailBean) {
        bean = t

    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
}