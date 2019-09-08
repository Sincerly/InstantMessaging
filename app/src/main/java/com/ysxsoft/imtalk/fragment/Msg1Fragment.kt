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
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.widget.SysCustomerBanner
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.fm_msg1.*
import io.rong.imkit.model.ConversationProviderTag
import org.litepal.LitePal
import org.litepal.extension.find


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
        myBroadcast = MyBroadcast()
        activity!!.registerReceiver(myBroadcast, intentFilter)

        mAdapter = MsgChatListAdapter(mContext)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mAdapter)
        recyclerView.setAdapter(mLuRecyclerViewAdapter)
        mLuRecyclerViewAdapter!!.addHeaderView(SysCustomerBanner(mContext))

        val divider = LuDividerDecoration.Builder(activity, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build()
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(divider)

        conversionList()
    }
    var userInfo:UserInfo?=null
    private fun conversionList() {
        RongIMClient.getInstance().getConversationList(object : RongIMClient.ResultCallback<MutableList<Conversation>>() {
            override fun onSuccess(datas: MutableList<Conversation>?) {
                if (datas != null) {
                    mAdapter!!.addAll(datas)
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
                val list = LitePal.where("uid=?", targetId).find<com.ysxsoft.imtalk.bean.UserInfo>()
                if(list.size>0){
                     userInfo = list.get(0)
                }
                when (type) {
                    Conversation.ConversationType.PRIVATE -> {
                        RongIM.getInstance().startPrivateChat(getActivity(), targetId, userInfo!!.nikeName);
                    }
                    Conversation.ConversationType.GROUP -> {
                        RongIM.getInstance().startGroupChat(getActivity(), targetId, "标题");
                    }
                    Conversation.ConversationType.SYSTEM -> {

                    }
                    Conversation.ConversationType.CUSTOMER_SERVICE -> {

                    }
                }
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