package com.ysxsoft.imtalk.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.PalMessageAdapter
import com.ysxsoft.imtalk.bean.EventBusBean
import com.ysxsoft.imtalk.bean.GroupIdBean
import com.ysxsoft.imtalk.bean.PalMessageBus
import com.ysxsoft.imtalk.bean.SysBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseApplication
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.view.*
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.fm_family_find.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import io.rong.imkit.fragment.ConversationFragment
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.RemoteHistoryMsgOption
import kotlinx.android.synthetic.main.head_wear_fragment_layout.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * 家族-发现
 */
class FamilyFindFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fm_family_find
    }

    //设置数据
    var list = ArrayList<String>()

    private lateinit var msgAdapter : PalMessageAdapter
    private var groupId = ""
    private var palMessages = ArrayList<Message>()

    /**
     * 收到新消息
     */
    @Subscribe
    @Synchronized fun palMessage( bus : PalMessageBus){
        val newMessage  = bus.newMessage
        if (groupId.isEmpty()){
            palMessages.add(newMessage)
        }else{
//            if (msgAdapter.){
//                msgAdapter.setDataList()
//            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        initView()
        requestData()
        requestGroupData()
    }

    private fun requestGroupData() {
        val map = HashMap<String, String>()
        map["uid"] = AuthManager.getInstance().currentUserId
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .groupId(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GroupIdBean> {
                    override fun onError(e: Throwable?) {
                        Log.e("onError", "onError")
                    }

                    override fun onNext(t: GroupIdBean?) {
                        if (t!!.code == 0) {
                            groupId = t.data
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun requestData() {
        val map = HashMap<String, String>()
        map.put("uid", AuthManager.getInstance().currentUserId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .sys_list(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SysBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SysBean?) {
                        if (t!!.code == 0) {
                            for (bean in t.data) {
                                list.add(bean.sys_title)
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })
        tv_marque_title.setDatas(list)
        tv_marque_title.startViewAnimator()
    }

    override fun onPause() {
        super.onPause()
        tv_marque_title.stopViewAnimator()
    }

    private fun initView() {
        msgAdapter = PalMessageAdapter(mContext)
        group_recyclerview.layoutManager = LinearLayoutManager(mContext)
        group_recyclerview.adapter = msgAdapter

        //土豪榜
        tv1.setOnClickListener {
            startActivity(TyrantListActivity::class.java)
        }
        //巨星榜
        tv2.setOnClickListener {
            startActivity(SupperStarActivity::class.java)
        }
        //周星榜
        tv3.setOnClickListener {
            startActivity(WeekStarActivity::class.java)
        }
        //签到
        tv4.setOnClickListener {
            startActivity(QDActivity::class.java)
        }

        ll_notice.setOnClickListener {
            NoticeActivity.starNoticeActivity(mContext, "1")
        }
        tv_marque_title.setItemOnClickListener { data, position ->
            NoticeActivity.starNoticeActivity(mContext, "1")
        }
        //交友大厅
        tv_look.setOnClickListener {
            PalLobbyActivity.intentPalLobbyActivity(groupId)
//            NoticeActivity.starNoticeActivity(mContext, "0")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}