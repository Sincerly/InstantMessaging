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
import com.ysxsoft.imtalk.adapter.MsgFsAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.FansListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_my_family.*
import kotlinx.android.synthetic.main.fm_msg21.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * 消息-联系人-粉丝
 */
class Msg23Fragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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
        return R.layout.fm_msg21
    }

    var mDataAdapter: MsgFsAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var page = 1
    var flag = 0

    override fun initUi() {
        initView()
    }
    private fun initView() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = MsgFsAdapter(mContext)
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
        mDataAdapter!!.setOnMsgFsListener(object : MsgFsAdapter.OnMsgFsListener {
            override fun onClick(position: Int) {
                val dataBean = mDataAdapter!!.dataList.get(position)
                when (dataBean.status) {
                    "1" -> {
                        flag = 1
                    }
                    "2" -> {
                        flag = 2
                    }
                }
                FouceData(dataBean.fs_id)
            }
        })
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

    private fun FouceData(id: String) {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("fs_id", id)
        map.put("flag", flag.toString())
        NetWork.getService(ImpService::class.java)
                .fans(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            onRefresh()
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
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("type", "2")
        NetWork.getService(ImpService::class.java)
                .fansList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FansListBean>{
                    override fun onError(e: Throwable?) {
                        showToastMessage("粉丝=="+e!!.message.toString())
                    }

                    override fun onNext(t: FansListBean?) {
                        if (t!!.code == 0) {
                            showData(t)
                            mDataAdapter!!.addAll(t.data)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.size)
                            notifyDataSetChanged()
                        } else {
                            mSwipeRefreshLayout.isRefreshing = false
                            mRecyclerView.setRefreshing(false)//同时调用LuRecyclerView的setRefreshing方法
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    var bean: FansListBean? = null

    private fun showData(t: FansListBean) {
        bean = t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            onRefresh()
        }
    }
}