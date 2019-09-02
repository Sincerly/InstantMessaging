package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.BankListAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserBankListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.DeleteBankCardDialog
import kotlinx.android.synthetic.main.activity_bank_card_list.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

class BankCardListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

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
        return R.layout.activity_bank_card_list
    }


    var mDataAdapter: BankListAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler(this)
    var page = 1
    var bean:UserBankListBean?=null
    var is_finish:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         is_finish = intent.getStringExtra("is_finish")
        setLightStatusBar(true)
        initStatusBar(topView)
        setTitle("我的银行卡")
        setBackVisibily()
        initView()
    }

    private fun initView() {
        tv_title_right.setOnClickListener {
            startActivity(BankCardEditActivity::class.java)
        }
        //添加银行卡
        rl_add.setOnClickListener {
            startActivity(BankCardEditActivity::class.java)
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = BankListAdapter(mContext);
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
            if ("1".equals(is_finish)) {
                val bank_id = mDataAdapter!!.dataList.get(position).bank_id.toString()
                val intent = Intent()
                intent.putExtra("bank_id", bank_id)
                setResult(1428, intent)
                finish()
            }
        }
        mLuRecyclerViewAdapter!!.setOnItemLongClickListener { view, position ->
            val bank_id = mDataAdapter!!.dataList.get(position).bank_id.toString()
            val dialog = DeleteBankCardDialog(mContext)
            dialog.setDeleteClickListener(object :DeleteBankCardDialog.DeleteClickListener{
                override fun deleteClick() {
                    DelData(bank_id)
                }
            })
            dialog.show()
        }
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

    private fun DelData(bank_id: String) {
        NetWork.getService(ImpService::class.java)
                .DelBankInfo(SpUtils.getSp(mContext,"uid"),bank_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommonBean>{
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            onRefresh()
                        }
                    }
                })

    }

    @SuppressLint("HandlerLeak")
    inner class PreviewHandler(activity: BankCardListActivity) : Handler() {
        var ref = WeakReference<BankCardListActivity>(activity);
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
                .UserBankList(SpUtils.getSp(mContext,"uid"),page.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<UserBankListBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserBankListBean?) {
                        if (t!!.code==0){
                            showData(t)
                            if(t.data.size<=0){
                                rl_add.visibility = View.VISIBLE;
                                tv_title_right.visibility=View.GONE
                                mSwipeRefreshLayout.visibility=View.GONE
                            }else{
                                rl_add.visibility = View.GONE;
                                mSwipeRefreshLayout.visibility=View.VISIBLE
                                tv_title_right.visibility=View.VISIBLE
                                tv_title_right.setText("添加")
                            }

                            mDataAdapter!!.addAll(t.data)
                            if (mSwipeRefreshLayout!!.isRefreshing()) {
                                mSwipeRefreshLayout!!.setRefreshing(false)
                            }
                            mRecyclerView.refreshComplete(t.data.size)
                            notifyDataSetChanged()
                        }else{
                            rl_add.visibility = View.VISIBLE;
                            tv_title_right.visibility=View.GONE
                            mSwipeRefreshLayout.visibility=View.GONE
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun showData(t: UserBankListBean) {
        bean=t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }
}
