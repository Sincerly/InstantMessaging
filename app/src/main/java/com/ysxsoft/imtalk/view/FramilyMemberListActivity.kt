package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.Jzcy2Adapter
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.JZListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_framily_member_list.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

class FramilyMemberListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

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


    companion object {
        fun starFramilyMemberListActivity(mContext: Context, fm_id: String, is_fmy: String) {
            val intent = Intent(mContext, FramilyMemberListActivity::class.java)
            intent.putExtra("fm_id", fm_id)
            intent.putExtra("is_fmy", is_fmy)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_framily_member_list
    }

    var mDataAdapter: Jzcy2Adapter? = null
    var fm_id: String? = null
    var is_fmy: String? = null
    var page = 1
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler(this)
    var bean:JZListBean?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm_id = intent.getStringExtra("fm_id")
        is_fmy = intent.getStringExtra("is_fmy")
        setLightStatusBar(true)
        initStatusBar(topView)
        setBackVisibily()
        setTitle("家族全部成员")
        initView()
    }

    private fun initView() {
        img_right.setImageResource(R.mipmap.search_right)
        //搜索
        img_right.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }

        mDataAdapter = Jzcy2Adapter(mContext)
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

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler(activity: FramilyMemberListActivity) : Handler() {
        var ref = WeakReference<FramilyMemberListActivity>(activity);
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
                    mRecyclerView.refreshComplete(bean!!.data.fmy_list.size)
                    notifyDataSetChanged()
                } else {
                    mRecyclerView.setOnNetWorkErrorListener(OnNetWorkErrorListener {
                        mRecyclerView.refreshComplete(bean!!.data.fmy_list.size)
                        notifyDataSetChanged()
                        requestData()
                    })
                }
            }
        }
    }

    private fun getData() {
        NetWork.getService(ImpService::class.java)
                .fm_list1(SpUtils.getSp(mContext, "uid"), fm_id!!, is_fmy!!, page.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JZListBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: JZListBean?) {
                        if (t!!.code == 0) {
                            tv_all_num.setText("全部成员（"+t.data.count+"人）")
                            showData(t)
                            mDataAdapter!!.addAll(t.data.fmy_list)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.fmy_list.size)
                            notifyDataSetChanged()
                        }else{
                            mSwipeRefreshLayout!!.setRefreshing(false)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun showData(t: JZListBean) {
        bean=t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
}
