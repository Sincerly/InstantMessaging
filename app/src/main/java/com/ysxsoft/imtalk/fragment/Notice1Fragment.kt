package com.ysxsoft.imtalk.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.PalLobbyAdapter
import com.ysxsoft.imtalk.utils.BaseFragment
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.fragment_pal_lobby.*

/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class Notice1Fragment:BaseFragment(){
    override fun getLayoutResId(): Int {
        return R.layout.notice1_fragment_layout
    }

    override fun onResume() {
        super.onResume()

    }


//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String) =
//                PalLobbyFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_GROUPID, param1)
//                    }
//                }
//    }
//
//    private lateinit var groupId : String
//    private lateinit var chatListAdapter : PalLobbyAdapter
//
//
//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        groupId = arguments!!.getString(ARG_GROUPID, "")
//    }
//
//    override fun getLayoutResId(): Int {
//        return R.layout.fragment_pal_lobby
//    }
//
//    override fun initUi() {
//        initAdapter()
//        initMessage()
//
//    }
//
//    //ResultCallback
//    private fun initAdapter(){
//        //聊天列表
//        chatListAdapter = PalLobbyAdapter(mContext)
//        recyclerView.layoutManager = LinearLayoutManager(mContext)
//        recyclerView.adapter = chatListAdapter
//    }
//
//    private fun  initMessage(){
//        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupId, object : RongIMClient.ResultCallback<Conversation>(){
//            override fun onSuccess(p0: Conversation?) {
//            }
//
//            override fun onError(p0: RongIMClient.ErrorCode?) {
//            }
//
//        })
//        RongIM.getInstance().getConversationList(object : RongIMClient.ResultCallback<List<Conversation>>(){
//            override fun onSuccess(p0: List<Conversation>?) {
//                p0?.let { chatListAdapter.setDataList(it) }
//            }
//            override fun onError(p0: RongIMClient.ErrorCode?) {
//            }
//        }, Conversation.ConversationType.GROUP)
//    }
}