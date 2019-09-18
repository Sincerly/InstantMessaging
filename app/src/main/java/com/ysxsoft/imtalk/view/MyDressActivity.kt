package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.mSwipeRefreshLayout
import com.ysxsoft.imtalk.adapter.MyDressAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.SGiftBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.my_dress_layout.*
import kotlinx.android.synthetic.main.room_tag_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDressActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

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
        return R.layout.my_dress_layout
    }

    var mDataAdapter: MyDressAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler(this)
    var page = 1
    var type = 2
    var beas: List<SGiftBean.DataBean.ListInfoBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackVisibily()
        setTitle("我的装扮")
        initView()
    }

    private fun initView() {
        tv_headwear.isSelected = true
        tv_headwear.setOnClickListener {
            type = 2
            if (tv_headwear.isSelected) {
                return@setOnClickListener
            }
            tv_headwear.isSelected = true
            tv_car.isSelected = false
            onRefresh()
        }
        tv_car.setOnClickListener {
            type = 1
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            tv_car.isSelected = true
            tv_headwear.isSelected = false
            onRefresh()
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = MyDressAdapter(mContext)

        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.adapter = mLuRecyclerViewAdapter
        val divider = LuDividerDecoration.Builder(mContext, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(divider)
        mDataAdapter!!.setOnUserItemListener(object : MyDressAdapter.OnUserItemListener {
            override fun userClick(position: Int) {
                val id = mDataAdapter!!.dataList.get(position).id.toString()
                val is_use = mDataAdapter!!.dataList.get(position).is_use.toString()
                if (is_use.equals("0")){
                    saveData(id,"1")
                }else{
                    saveData(id,"0")
                }
            }
        })

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent)
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧")
    }

    private fun saveData(id: String, is_use: String) {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("ts_id",id)
//        if (type == 1) {
//            map.put("type", "2")
//        } else {
            map.put("type", type.toString())
//        }
        map.put("status", is_use)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .set_default(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            onRefresh()
                        }
                    }
                })
    }

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler(activity: MyDressActivity) : Handler() {
        var ref = WeakReference<MyDressActivity>(activity);
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
//        val map = HashMap<String, String>()
//        map.put("type",type.toString())
//        map.put("uid",SpUtils.getSp(mContext,"uid"))
//        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .my_gift(SpUtils.getSp(mContext,"uid"),type.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SGiftBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message.toString())
                    }

                    override fun onNext(t: SGiftBean?) {
                        if (t!!.code == 0) {
                            showData(t.data.listInfo)

                            mDataAdapter!!.addAll(t.data.listInfo)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.listInfo.size)
                            notifyDataSetChanged()
                        }else{
                            mSwipeRefreshLayout!!.setRefreshing(false)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun showData(data: List<SGiftBean.DataBean.ListInfoBean>) {
        beas = data
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }
}