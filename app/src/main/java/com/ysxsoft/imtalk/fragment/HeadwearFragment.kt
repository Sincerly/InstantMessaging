package com.ysxsoft.imtalk.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.DressMallAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.bean.SGiftBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.MyDressActivity
import kotlinx.android.synthetic.main.head_wear_fragment_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class HeadwearFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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
        return R.layout.head_wear_fragment_layout
    }

    var mDataAdapter: DressMallAdapter? = null
    var page = 1
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var beas: List<DressMallBean.DataBean>? = null
    var auto_id: String? = null

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        tv_buy.setOnClickListener {
            if(TextUtils.isEmpty(auto_id)){
                showToastMessage("头饰不能为空")
                return@setOnClickListener
            }
            BuyData()
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = DressMallAdapter(mContext)
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.adapter = mLuRecyclerViewAdapter
        val divider = LuDividerDecoration.Builder(mContext, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(divider)

        mLuRecyclerViewAdapter!!.setOnItemClickListener { view, position ->
            val bean = mDataAdapter!!.dataList.get(position)
            auto_id = bean.id.toString()
            tv_money.setText(bean.gold + "金币")
            tv_day.setText("/" + bean.days + "天")
            mDataAdapter!!.setSelect(position)
        }

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
        onRefresh()
    }

    private fun BuyData() {
        NetWork.getService(ImpService::class.java)
                .buyDressUp("2", auto_id!!, SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            startActivity(MyDressActivity::class.java)
                        }
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
                    mRecyclerView.refreshComplete(beas!!.size)
                    notifyDataSetChanged()
                } else {
                    mRecyclerView.setOnNetWorkErrorListener(OnNetWorkErrorListener {
                        mRecyclerView.refreshComplete(beas!!.size)
                        notifyDataSetChanged()
                        requestData()
                    })
                }
            }
        }
    }

    private fun getData() {
        NetWork.getService(ImpService::class.java)
                .DressMall("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DressMallBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: DressMallBean?) {
                        if (t!!.code == 0) {
                            showData(t.data)
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

    private fun showData(data: List<DressMallBean.DataBean>) {
        beas = data
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

}