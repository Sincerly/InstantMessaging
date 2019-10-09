package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMemListBean
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.CircleImageView
import kotlinx.android.synthetic.main.activity_online_list.*
import kotlinx.android.synthetic.main.include_onlyrecyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.toolBar
import kotlinx.android.synthetic.main.include_toolbar.tvTitle
import kotlinx.android.synthetic.main.online_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OnlineListActivity : BaseActivity() {

    companion object {
        fun starOnlineListActivity(mContext: Context, roomId: String) {
            val intent = Intent(mContext, OnlineListActivity::class.java)
            intent.putExtra("roomId", roomId)
            mContext.startActivity(intent)
        }
    }

    private lateinit var adapter: BaseQuickAdapter<RoomMemListBean.DataBean.RoomUserListBean, BaseViewHolder>

    override fun getLayout(): Int {
        return R.layout.activity_online_list
    }

    var roomId: String? = null
    var userList: List<RoomMemListBean.DataBean.RoomUserListBean>? = null
    override fun initUi() {
        initToolBar(viewTop)
        setStatusBarFullTransparent()
        roomId = intent.getStringExtra("roomId")

        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        initData()
        initAdapter()
    }

    private fun initData() {
        NetWork.getService(ImpService::class.java)
                .RoomUserList(roomId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RoomMemListBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomMemListBean?) {
                        if (t!!.code == 0) {
                            userList = t.data.roomUserList
//                            mRecyclerView.adapter = adapter
//                            adapter!!.addAll(userList)
                            tvTitle.text = "在线人数("+t.data.roomUserList.size+")"
                            adapter = object : BaseQuickAdapter<RoomMemListBean.DataBean.RoomUserListBean, BaseViewHolder>(R.layout.item_online, userList) {
                                override fun convert(helper: BaseViewHolder?, item: RoomMemListBean.DataBean.RoomUserListBean?) {
                                    ImageLoadUtil.GlideHeadImageLoad(mContext, item!!.icon, helper?.getView<CircleImageView>(R.id.ivAvatar)!!)
                                    helper.getView<TextView>(R.id.tvNick)!!.text = item.nickname

                                    if (item.sex==1) {
                                        helper.getView<ImageView>(R.id.ivSex)!!.setImageResource(R.mipmap.img_boy)
                                    } else {
                                        helper.getView<ImageView>(R.id.ivSex)!!.setImageResource(R.mipmap.img_girl)
                                    }
                                    val charmIcon = GradeIconUtils.charmIcon(item.ml_level.toInt())
                                    helper.getView<ImageView>(R.id.img_mei)!!.setImageResource(charmIcon[0])
                                    helper.getView<TextView>(R.id.tvTag1).setTextColor(charmIcon[1])
                                    helper.getView<TextView>(R.id.tvTag1)!!.text = item.ml_level
                                    val gradeIcon = GradeIconUtils.gradeIcon(item.user_level.toInt())
                                    helper.getView<ImageView>(R.id.img_zs)!!.setImageResource(gradeIcon[0])
                                    helper.getView<TextView>(R.id.tvTag2)!!.setTextColor(gradeIcon[1])
                                    helper.getView<TextView>(R.id.tvTag2)!!.text = item.user_level

                                    when (item.role) {
                                        "1" -> {
                                            helper.getView<TextView>(R.id.tvTag)!!.visibility=View.VISIBLE
                                            helper.getView<TextView>(R.id.tvTag)!!.text = "房主"
                                        }
                                        "2" -> {
                                            helper.getView<TextView>(R.id.tvTag)!!.visibility=View.VISIBLE
                                            helper.getView<TextView>(R.id.tvTag)!!.text = "管理员"
                                        }
                                        else->{
                                            helper.getView<TextView>(R.id.tvTag)!!.visibility=View.GONE
                                        }
                                    }
                                    helper.itemView.setOnClickListener {
                                        if (AuthManager.getInstance().currentUserId.equals(item.uid)){
                                            MyDataActivity.startMyDataActivity(mContext,item.uid,"myself")
                                        }else{
                                            MyDataActivity.startMyDataActivity(mContext,item.uid,"")
                                        }

                                    }
                                    val show = if (helper?.adapterPosition ?: 0 == 0) View.GONE else View.VISIBLE
                                    helper?.getView<View>(R.id.viewLines)?.visibility = show
                                }
                            }
                            recyclerView.layoutManager = LinearLayoutManager(mContext)
                            recyclerView.adapter = adapter

                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initAdapter() {

//        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
//            showToastMessage(position.toString())
//        }
    }

}
