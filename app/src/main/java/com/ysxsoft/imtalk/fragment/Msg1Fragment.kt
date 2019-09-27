package com.ysxsoft.imtalk.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.MsgChatListAdapter
import com.ysxsoft.imtalk.bean.SysMessageBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.SysCustomerBanner
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.fm_msg1.*
import io.rong.imkit.model.ConversationProviderTag
import kotlinx.android.synthetic.main.sys_customer_layout.view.*
import org.litepal.LitePal
import org.litepal.extension.find
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * 消息-消息
 */
@ConversationProviderTag
class Msg1Fragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fm_msg1
    }

    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    var mAdapter: MsgChatListAdapter? = null
    var myBroadcast: MyBroadcast? = null

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("RECEIVEMESSAGE")
        if (myBroadcast == null) {
            myBroadcast = MyBroadcast()
        }
        activity!!.registerReceiver(myBroadcast, intentFilter)

        mAdapter = MsgChatListAdapter(mContext)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mAdapter)
        recyclerView.setAdapter(mLuRecyclerViewAdapter)
//        mLuRecyclerViewAdapter!!.addHeaderView(SysCustomerBanner(mContext))

        val divider = LuDividerDecoration.Builder(activity, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build()
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(divider)

        requestHeadData()
        conversionList()
    }

    var userInfo: UserInfo? = null
    var posittion: Conversation ?= null
    private fun conversionList() {
        RongIMClient.getInstance().getConversationList(object : RongIMClient.ResultCallback<MutableList<Conversation>>() {
            override fun onSuccess(datas: MutableList<Conversation>?) {
                Log.e("---->", "" + datas?.size)
                if (datas != null) {
                    for (bean in datas){
                        if (bean.targetId.equals(PalLobbyGrade.groupId)){
                            posittion=bean
                        }
                    }
                    datas.remove(posittion)
                    mAdapter!!.setDataList(datas)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onError(p0: RongIMClient.ErrorCode?) {
                Log.d("tag", "会话列表==" + p0)
            }
        })
        mAdapter!!.setOnChatMsgListener(object : MsgChatListAdapter.OnChatMsgListener {
            override fun onClick(position: Int) {

                val targetId = mAdapter!!.dataList.get(position).targetId
                val type = mAdapter!!.dataList.get(position).conversationType
                val list = LitePal.where("uid=?", targetId).find<UserInfo>()
                if (list.size > 0) {
                    userInfo = list.get(0)
                }
                when (type) {
                    Conversation.ConversationType.PRIVATE -> {
                        if (userInfo!=null){
                            RongIM.getInstance().startPrivateChat(getActivity(), targetId, userInfo!!.nikeName)
                        }
                    }
                    Conversation.ConversationType.GROUP -> {
                        RongIM.getInstance().startGroupChat(getActivity(), targetId, "嗨嗨聊天群组");
                    }
                    Conversation.ConversationType.SYSTEM -> {

                    }
                    Conversation.ConversationType.CUSTOMER_SERVICE -> {

                    }
                }
            }
        })
    }

    private fun requestHeadData() {
        NetWork.getService(ImpService::class.java)
                .sysmessage(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SysMessageBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SysMessageBean?) {
                        if (t!!.code == 0) {
                            for (bean in t.data.userInfo) {
                                var tempUser = UserInfo()
                                tempUser.uid = "" + bean.id             //UID
                                tempUser.nikeName = bean.nickname   //昵称
                                tempUser.icon = bean.icon          //头像
                                tempUser.isSys = true
                                tempUser.save()
                                RongIMClient.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, "" + bean.id, true)
                            }
                            if (mAdapter != null) {
                                mAdapter!!.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    inner class MyBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ("RECEIVEMESSAGE".equals(intent!!.action)) {
                conversionList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(myBroadcast)
    }

}