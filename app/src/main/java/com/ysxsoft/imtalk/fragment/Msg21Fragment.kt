package com.ysxsoft.imtalk.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.MsgHyAdapter
import com.ysxsoft.imtalk.bean.FansListBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.BankCardListActivity
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.activity_my_family.*
import kotlinx.android.synthetic.main.fm_msg21.*
import org.litepal.LitePal
import org.litepal.extension.findAll
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * 消息-联系人-好友
 */
class Msg21Fragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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

    var mDataAdapter: MsgHyAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    val mHandler = PreviewHandler()
    var page = 1
    var baseHelper: SQLiteOpenHelper? = null
    var db: SQLiteDatabase? = null
    private val TABLE_NAME = "RoomUserListBean"

    override fun onResume() {
        super.onResume()
        initView()

    }

    override fun initUi() {

    }

    private fun initView() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48))
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color)
            mSwipeRefreshLayout.setOnRefreshListener(this)
        }
        mDataAdapter = MsgHyAdapter(mContext)
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
            RongIM.getInstance().startPrivateChat(mContext, mDataAdapter!!.dataList.get(position).fs_id.toString(), "标题");
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
        map.put("type", "3")
        NetWork.getService(ImpService::class.java)
                .fansList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<FansListBean> {
                    override fun call(t: FansListBean?) {
                        if (t!!.code == 0) {
                            showData(t)
                            for (bean in t.data) {
                                val info = UserInfo()
                                info.uid = bean.fs_id
                                info.icon = bean.icon
                                info.nikeName = bean.nickname
                                info.sex = bean.sex
                                info.zsl = bean.th_level
                                info.save()
                            }
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
                })


    }

    var bean: FansListBean? = null
    private fun showData(t: FansListBean) {
        bean = t
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }

}