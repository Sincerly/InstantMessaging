package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.JzQzAdapter
import com.ysxsoft.imtalk.adapter.Jzcy1Adapter
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_my_family.*
import kotlinx.android.synthetic.main.title_layout2.*
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.ShareFriendDialog
import io.rong.imkit.RongIM
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


class MyFamilyActivity : BaseActivity() {
    companion object {
        fun startMyFamilyActivity(mContext: Context, fm_id: String, is_fmy: String) {
            val intent = Intent(mContext, MyFamilyActivity::class.java)
            intent.putExtra("fm_id", fm_id)
            intent.putExtra("is_fmy", is_fmy)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_my_family
    }

    var mAdapter: Jzcy1Adapter? = null
    var adapter: JzQzAdapter? = null
    var fm_id: String? = null
    var is_fmy: String? = null
    var Jzuid: String? = null
    var fmy_list: MutableList<JZListBean.DataBean.FmyListBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity_my_family_item
        fm_id = intent.getStringExtra("fm_id")
        is_fmy = intent.getStringExtra("is_fmy")
        setLightStatusBar(false)
        initStatusBar(topView)
        when (is_fmy) {
            "1" -> {//族长
                img_right.visibility = View.INVISIBLE
                ll_role_zz.visibility = View.GONE
                tv_join_jz.visibility = View.GONE
                ll_role_person.visibility = View.VISIBLE
                tv_new_add.visibility = View.VISIBLE
            }
            "2" -> {//非族长
                tv_new_add.visibility = View.GONE
                ll_role_zz.visibility = View.GONE
                tv_join_jz.visibility = View.GONE
                ll_role_person.visibility = View.VISIBLE
                img_right.visibility = View.VISIBLE
            }
            "3" -> {//未加入
                ll_role_zz.visibility = View.VISIBLE
                tv_join_jz.visibility = View.VISIBLE
                ll_role_person.visibility = View.GONE
                tv_new_add.visibility = View.GONE
                img_right.visibility = View.INVISIBLE
            }
        }
        initView()
        requestData()
        JzData()
        JzListData()
        QZData()
    }

    private fun QZData() {
        NetWork.getService(ImpService::class.java)
                .groupList(SpUtils.getSp(mContext, "uid"), fm_id!!, is_fmy!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<GroupListBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: GroupListBean?) {
                        if (t!!.code == 0) {
                            tv_familly_qun.setText("家族群组（" + t.data.count + "）")
                            if (t.data.groupInfo != null) {
                                adapter = JzQzAdapter(mContext)
                                recyclerView2.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
                                recyclerView2.setNestedScrollingEnabled(false);
                                recyclerView2.layoutManager = LinearLayoutManager(mContext)
                                recyclerView2.adapter = adapter
                                adapter!!.addAll(t.data.groupInfo)
                                adapter!!.setOnJoinListener(object : JzQzAdapter.OnJoinListener {
                                    override fun onClick(position: Int) {
                                        NetWork.getService(ImpService::class.java)
                                                .JoinGroup(SpUtils.getSp(mContext, "uid"), fm_id!!, adapter!!.dataList.get(position).id)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(object : Observer<CommonBean> {
                                                    override fun onError(e: Throwable?) {
                                                        showToastMessage(e!!.message.toString())
                                                    }

                                                    override fun onNext(t: CommonBean?) {
                                                        showToastMessage(t!!.msg)
                                                        if (t.code == 0) {
                                                            QZData()
                                                            adapter!!.notifyDataSetChanged()
                                                        }
                                                    }

                                                    override fun onCompleted() {
                                                    }
                                                })
                                    }
                                })
                                adapter!!.setOnItemClickListener(object : JzQzAdapter.OnItemClickListener{
                                    override fun onItemClick(position: Int, title: String, groupId: String) {
                                        RongIM.getInstance().startGroupChat(mContext, groupId, title);
                                    }
                                });
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun JzListData() {
        NetWork.getService(ImpService::class.java)
                .fm_list1(SpUtils.getSp(mContext, "uid"), fm_id!!, is_fmy!!, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JZListBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: JZListBean?) {
                        if (t!!.code == 0) {
                            if (t.data.fmy_list != null) {
                                fmy_list = t.data.fmy_list
                                mAdapter = Jzcy1Adapter(mContext)
                                val linearLayoutManager = LinearLayoutManager(mContext)
                                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                recyclerView.layoutManager = linearLayoutManager
                                recyclerView.adapter = mAdapter
                                mAdapter!!.addAll(fmy_list!!)
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .myfm(SpUtils.getSp(mContext, "uid"), fm_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MyFmBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: MyFmBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.fmy_pic, iv_tx)
                            tv_jz_name.setText(t.data.fmy_name)
                            tv_jz_id.setText("家族ID:" + t.data.fmy_sn)
                            tv_jz_num.setText("成员:" + t.data.fmy_num)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    fun JzData() {
        NetWork.getService(ImpService::class.java)
                .fm_list(SpUtils.getSp(mContext, "uid"), fm_id!!, is_fmy!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FMListBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message.toString())
                    }

                    override fun onNext(t: FMListBean?) {
                        if (t!!.code == 0) {
                            Jzuid = t.data.uid
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                            tv_name.setText(t.data.nickname)
                        }
                    }

                    override fun onCompleted() {

                    }
                })
    }

    private fun initView() {
        setBackVisibily()
        setTitle("我的家族")
        img_right.setImageResource(R.mipmap.img_tc)
        img_right2.setImageResource(R.mipmap.share_white)
        //退出家族
        img_right.setOnClickListener {
            NetWork.getService(ImpService::class.java)
                    .Tcfm(SpUtils.getSp(mContext, "uid"), fm_id!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CommonBean> {
                        override fun onError(e: Throwable?) {
                            showToastMessage(e!!.message.toString())
                        }

                        override fun onNext(t: CommonBean?) {
                            showToastMessage(t!!.msg)
                            if (t.code == 0) {
                                finish()
                            }
                        }

                        override fun onCompleted() {
                        }
                    })
        }

        img_right2.setOnClickListener {
            ShareFriendDialog(mContext).show()
        }
        //查看全部成员
        tv_look_list.setOnClickListener {
//            startActivity(FramilyMemberListActivity::class.java)
            FramilyMemberListActivity.starFramilyMemberListActivity(mContext,fm_id!!,is_fmy!!)
        }
        ll_role_zz.setOnClickListener {
            MyDataActivity.startMyDataActivity(mContext, Jzuid!!, "2")
        }
        tv_join_jz.setOnClickListener {
            JoinData(fm_id)
        }
    }

    private fun JoinData(fm_id: String?) {
        NetWork.getService(ImpService::class.java)
                .Joinfm(SpUtils.getSp(mContext, "uid"), fm_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage("我的家族==" + e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            JzListData()
                            tv_join_jz.visibility = View.GONE
                            ll_role_zz.visibility = View.GONE
                            ll_role_person.visibility = View.VISIBLE
                            tv_new_add.visibility = View.GONE
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
