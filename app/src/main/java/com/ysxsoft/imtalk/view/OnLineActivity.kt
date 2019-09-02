package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.OnLineAdapter
import com.ysxsoft.imtalk.bean.RoomMemListBean
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.model.RoomUserListBean
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.online_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class OnLineActivity : BaseActivity() {

    companion object {
        fun starOnLineActivity(mContext: Context, roomId: String) {
            val intent = Intent(mContext, OnLineActivity::class.java)
            intent.putExtra("roomId", roomId)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.online_layout
    }

    var roomId: String? = null
    var adapter: OnLineAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra("roomId")
        setTitle("在线听众")
        setBackVisibily()
        initView()
        initData()
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
                            val userList = t.data.roomUserList
                            mRecyclerView.adapter = adapter
                            adapter!!.addAll(userList)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun initView() {
        adapter = OnLineAdapter(mContext)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)

        adapter!!.setOnLineListener(object :OnLineAdapter.OnLineListener{
            override fun lineClick(position: Int) {
                if (!TextUtils.isEmpty(roomId)) {
                    val uid = adapter!!.dataList.get(position).uid
                    val intent = Intent()
                    intent.putExtra("userId", uid)
                    setResult(1035, intent)
                    finish()
                }
            }
        })
    }

}