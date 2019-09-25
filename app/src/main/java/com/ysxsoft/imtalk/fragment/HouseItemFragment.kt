package com.ysxsoft.imtalk.fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.listener.OnBannerListener
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.BannerDetailActivity
import com.ysxsoft.imtalk.view.ChatRoomActivity
import com.ysxsoft.imtalk.widget.banner.GlideImageLoader
import com.ysxsoft.imtalk.widget.dialog.RoomLockDialog
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.fragment_house_item.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

private const val ARG_POSITION = "position"

/**
 * 房间子页面 （推荐，女神，男神，娱乐，情感电台。。。）
 */
class HouseItemFragment : BaseFragment(), OnBannerListener, SwipeRefreshLayout.OnRefreshListener {
    var customDialog: CustomDialog? = null
    override fun onRefresh() {
        initUi()
    }

    override fun OnBannerClick(position: Int) {
        val banner_id = datas!!.get(position).id.toString()
        val intent = Intent(mContext, BannerDetailActivity::class.java)
        intent.putExtra("banner_id", banner_id)
        startActivity(intent)
    }

    lateinit var adapterHouse: BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterRecommend: BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterHouse1: BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterRecommend1: BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>

    private var position = 0
    private var pids: String? = null
    var mydatabean: UserInfoBean? = null
    override fun getLayoutResId(): Int {
        return R.layout.fragment_house_item
    }

    var datas: MutableList<BannerBean.DataBean>? = null
    var urls = ArrayList<String>() as MutableList<String>
    var tuijainDatas: MutableList<HomeRoomBean.DataBean.RoomListBean>? = null
    var hotDatas: MutableList<HomeRoomBean.DataBean.RoomListBean>? = null
    var roomLists: MutableList<HomeFRoomBean.DataBean.RoomListBean>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        position = arguments?.getInt(ARG_POSITION) ?: position
        pids = arguments!!.getString("pids")
    }

    private fun requestMyData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            customDialog!!.dismiss()
                            mydatabean = t
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun LunBoData() {
        NetWork.getService(ImpService::class.java)
                .HomeBanner("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BannerBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", "HouseItemFragment=LunBoData" + e!!.message.toString())
                    }

                    override fun onNext(t: BannerBean?) {
                        if (t!!.code == 0) {
                            datas = t.data
                            if (urls.size > 0) {
                                urls.clear()
                            }
                            for (bean in t.data) {
                                urls.add(bean.pic)
                            }
                            banner.setImages(urls)
                                    .setImageLoader(GlideImageLoader())
                                    .setOnBannerListener(this@HouseItemFragment)
                                    .start()
                        }
                    }

                    override fun onCompleted() {
                    }

                })

    }

    private fun NOHotData(pids: String) {
        NetWork.getService(ImpService::class.java)
                .RoomList1(pids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeFRoomBean> {
                    override fun onError(e: Throwable?) {
                        Log.d(this@HouseItemFragment.javaClass.name, e!!.message.toString())
                    }

                    override fun onNext(t: HomeFRoomBean?) {
                        if (t!!.code == 0) {
                            customDialog!!.dismiss()
                            refreshLayout.isRefreshing = false
                            roomLists = t.data.get(0).roomList
                            init1Adapter()
                        } else {
                            refreshLayout.isRefreshing = false
                            customDialog!!.dismiss()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun init1Adapter() {
        if (roomLists!!.size > 3) {
            adapterRecommend1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, roomLists!!.subList(0, 3)) {
                override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                    helper.getView<TextView>(R.id.tv_Content).text = item.room_name
                    if (TextUtils.isEmpty(item.memCount)) {
                        helper.getView<TextView>(R.id.tv_person).text = "0"
                    } else {
                        helper.getView<TextView>(R.id.tv_person).text = item.memCount
                    }
                    if (!"0".equals(item.is_lock)) {
                        helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.VISIBLE
                    } else {
                        helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.GONE
                    }
                    helper.itemView.setOnClickListener {
                        activity!!.sendBroadcast(Intent("WINDOW"))
                        NetWork.getService(ImpService::class.java)
                                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer<UserInfoBean> {
                                    override fun onError(e: Throwable?) {
                                    }

                                    override fun onNext(t: UserInfoBean?) {
                                        if (t!!.code == 0) {
                                            val data = t.data
                                            if (!TextUtils.isEmpty(mydatabean!!.data.now_roomId)) {
                                                if (TextUtils.equals(mydatabean!!.data.now_roomId, item.room_id.toString())) {
                                                    roomLock(item.room_id.toString())
                                                } else {
                                                    quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId,item.room_id.toString())
                                                }
                                            } else {
                                                roomLock(item.room_id.toString())
                                            }
                                        }
                                    }

                                    override fun onCompleted() {
                                    }
                                })
                    }
                }
            }
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.adapter = adapterRecommend1

            adapterHouse1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_room, roomLists!!.subList(3, roomLists!!.size)) {
                override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                    helper.getView<TextView>(R.id.tv_name).text = item.room_name
                    if (!"0".equals(item.is_lock)) {
                        helper.getView<ImageView>(R.id.img_b_lock)!!.visibility = View.VISIBLE
                    } else {
                        helper.getView<ImageView>(R.id.img_b_lock)!!.visibility = View.GONE
                    }
                    if (TextUtils.isEmpty(item.label_name)) {
                        helper.getView<TextView>(R.id.tv_Tag).text = "暂无"
                    } else {
                        helper.getView<TextView>(R.id.tv_Tag).text = item.label_name
                    }
                    if (TextUtils.isEmpty(item.memCount)) {
                        helper.getView<TextView>(R.id.tv_Online).text = "0" + "人在线"
                    } else {
                        helper.getView<TextView>(R.id.tv_Online).text = item.memCount + "人在线"
                    }
                    helper.itemView.setOnClickListener {
                        activity!!.sendBroadcast(Intent("WINDOW"))
                        NetWork.getService(ImpService::class.java)
                                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer<UserInfoBean> {
                                    override fun onError(e: Throwable?) {
                                    }

                                    override fun onNext(t: UserInfoBean?) {
                                        if (t!!.code == 0) {
                                            val data = t.data
                                            if (!TextUtils.isEmpty(data.now_roomId)) {
                                                if (TextUtils.equals(data.now_roomId, item.room_id.toString())) {
                                                    roomLock(item.room_id.toString())
                                                } else {
                                                    quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId,item.room_id.toString())
                                                }
                                            } else {
                                                roomLock(item.room_id.toString())
                                            }
                                        }
                                    }

                                    override fun onCompleted() {
                                    }
                                })
                    }
                }
            }
            recyclerViewRoom.layoutManager = LinearLayoutManager(mContext)
            recyclerViewRoom.adapter = adapterHouse1

        } else {
            adapterRecommend1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, roomLists) {
                override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                    helper.getView<TextView>(R.id.tv_Content).text = item.room_name
                    if (TextUtils.isEmpty(item.memCount)) {
                        helper.getView<TextView>(R.id.tv_person).text = "0"
                    } else {
                        helper.getView<TextView>(R.id.tv_person).text = item.memCount
                    }
                    if (!"0".equals(item.is_lock)) {
                        helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.VISIBLE
                    } else {
                        helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.GONE
                    }

                    helper.itemView.setOnClickListener {
                        NetWork.getService(ImpService::class.java)
                                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer<UserInfoBean> {
                                    override fun onError(e: Throwable?) {
                                    }

                                    override fun onNext(t: UserInfoBean?) {
                                        if (t!!.code == 0) {
                                            val data = t.data
                                            if (!TextUtils.isEmpty(data.now_roomId)) {
                                                if (TextUtils.equals(data.now_roomId, item.room_id.toString())) {
                                                    roomLock(item.room_id.toString())
                                                } else {
                                                    quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId,item.room_id.toString())
                                                }
                                            } else {
                                                roomLock(item.room_id.toString())
                                            }
                                        }
                                    }

                                    override fun onCompleted() {
                                    }
                                })
                    }
                }
            }
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.adapter = adapterRecommend1
        }
    }

    private fun tuiJianData(pids: String) {
        NetWork.getService(ImpService::class.java)
                .RoomList(pids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeRoomBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", e!!.message.toString())
                    }

                    override fun onNext(t: HomeRoomBean?) {
                        if (t!!.code == 0) {
                            customDialog!!.dismiss()
                            refreshLayout.isRefreshing = false
                            if (t.data.size > 0) {
                                when (t.data.size) {
                                    1 -> {
                                        tv_gftj.setText(t.data.get(0).cname)
                                        tuijainDatas = t.data.get(0).roomList
                                    }
                                    2 -> {
                                        tv_gftj.setText(t.data.get(0).cname)
                                        tuijainDatas = t.data.get(0).roomList
                                        hotDatas = t.data.get(1).roomList
                                    }
                                }
                                initAdapter()
                            }

                        } else {
                            refreshLayout.isRefreshing = false
                            customDialog!!.dismiss()
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    override fun initUi() {
        customDialog = CustomDialog(mContext, "正在加载....")
        customDialog!!.show()
        requestMyData()
        banner.visibility = if (0 == position) View.VISIBLE else View.GONE
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeResources(R.color.btn_color)
        LunBoData()
        if (position == 0) {
            tv_gftj.visibility = View.VISIBLE
            tuiJianData(pids!!)
        } else {
            tv_gftj.visibility = View.GONE
            NOHotData(pids!!)
        }
    }

    private fun initAdapter() {
        adapterRecommend = object : BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, tuijainDatas) {
            override fun convert(helper: BaseViewHolder, item: HomeRoomBean.DataBean.RoomListBean) {
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                helper.getView<TextView>(R.id.tv_Content).text = item.room_name
                if (TextUtils.isEmpty(item.memCount)) {
                    helper.getView<TextView>(R.id.tv_person).text = "0"
                } else {
                    helper.getView<TextView>(R.id.tv_person).text = item.memCount
                }
                if (!"0".equals(item.is_lock)) {
                    helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.VISIBLE
                } else {
                    helper.getView<ImageView>(R.id.img_w_lock)!!.visibility = View.GONE
                }

                helper.itemView.setOnClickListener {
                    activity!!.sendBroadcast(Intent("WINDOW"))
                    NetWork.getService(ImpService::class.java)
                            .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<UserInfoBean> {
                                override fun onError(e: Throwable?) {
                                }

                                override fun onNext(t: UserInfoBean?) {
                                    if (t!!.code == 0) {
                                        val data = t.data
                                        if (!TextUtils.isEmpty(data.now_roomId)) {
                                            if (TextUtils.equals(data.now_roomId, item.room_id.toString())) {
                                                roomLock(item.room_id.toString())
                                            } else {
                                                quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId,item.room_id.toString())
                                            }
                                        } else {
                                            roomLock(item.room_id.toString())
                                        }
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })
                }
            }
        }
//        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        val manager = LinearLayoutManager(mContext)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapterRecommend

        adapterHouse = object : BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_room, hotDatas) {
            override fun convert(helper: BaseViewHolder, item: HomeRoomBean.DataBean.RoomListBean) {
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                helper.getView<TextView>(R.id.tv_name).text = item.room_name
                if (!"0".equals(item.is_lock)) {
                    helper.getView<ImageView>(R.id.img_b_lock)!!.visibility = View.VISIBLE
                } else {
                    helper.getView<ImageView>(R.id.img_b_lock)!!.visibility = View.GONE
                }
                if (TextUtils.isEmpty(item.label_name)) {
                    helper.getView<TextView>(R.id.tv_Tag).text = "暂无"
                } else {
                    helper.getView<TextView>(R.id.tv_Tag).text = item.label_name
                }
                if (TextUtils.isEmpty(item.memCount)) {
                    helper.getView<TextView>(R.id.tv_Online).text = "0" + "人在线"
                } else {
                    helper.getView<TextView>(R.id.tv_Online).text = item.memCount + "人在线"
                }
                helper.itemView.setOnClickListener {
                    activity!!.sendBroadcast(Intent("WINDOW"))
                    NetWork.getService(ImpService::class.java)
                            .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<UserInfoBean> {
                                override fun onError(e: Throwable?) {
                                }

                                override fun onNext(t: UserInfoBean?) {
                                    if (t!!.code == 0) {
                                        val data = t.data
                                        if (!TextUtils.isEmpty(data.now_roomId)) {
                                            if (TextUtils.equals(data.now_roomId, item.room_id.toString())) {
                                                roomLock(item.room_id.toString())
                                            } else {
                                                quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId,item.room_id.toString())
                                            }
                                        } else {
                                            roomLock(item.room_id.toString())
                                        }
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })
                }
            }
        }
        recyclerViewRoom.layoutManager = LinearLayoutManager(mContext)
        recyclerViewRoom.adapter = adapterHouse

    }

    override fun onDestroy() {
        super.onDestroy()
        if (banner != null) {
            banner.stopAutoPlay()
        }
    }

    fun joinChatRoom(roomId: String, lock_pwd: String) {
        if (mydatabean == null) {
            showToastMessage("请稍后")
            return
        }
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, lock_pwd, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                val message = RoomMemberChangedMessage()
                message.setCmd(1)
                val carName = SharedPreferencesUtils.getCarName(mContext)
                val carPic = SharedPreferencesUtils.getCarPic(mContext)
                message.carName = carName//座驾名称
                message.carPic = carPic//座驾图片
                message.targetUserId = SpUtils.getSp(mContext, "uid")
                message.targetPosition = -1
                message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), mydatabean!!.data.nickname, Uri.parse(mydatabean!!.data.icon))
                val obtain = Message.obtain(result!!.roomInfo.room_id, Conversation.ConversationType.CHATROOM, message)
                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                    override fun onAttached(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                    }

                    override fun onSuccess(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                        if (TextUtils.equals(mydatabean!!.data.now_roomId, roomId)) {
                            ChatRoomActivity.starChatRoomActivity(mContext, roomId, mydatabean!!.data.nickname, mydatabean!!.data.icon, "identical")
                        } else {
                            ChatRoomActivity.starChatRoomActivity(mContext, roomId, mydatabean!!.data.nickname, mydatabean!!.data.icon, "")
                        }
                    }

                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                        Log.d("tag", p0!!.content.toString())
                    }
                });
            }

            override fun onFail(errorCode: Int) {
                Log.d("tag", "HouseItemFragment==加入房间==" + errorCode)
            }
        })
    }

    fun roomLock(roomId: String) {
        val map = HashMap<String, String>()
        map.put("room_id", roomId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .room_lock(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RoomLockBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomLockBean?) {
                        if (t!!.code == 0) {
                            RoomManager.getInstance().getRoomDetailInfo1(roomId, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(result: DetailRoomInfo?) {
                                    if (result != null) {
                                        if (AuthManager.getInstance().currentUserId.equals(result.roomInfo.uid)) {
                                            joinChatRoom(roomId, "")
                                        } else {
                                            var roomLock = t.data
                                            if (roomLock == 1) {
                                                val roomLockDialog = RoomLockDialog(mContext)
                                                roomLockDialog.setEdClickListener(object : RoomLockDialog.EdClickListener {
                                                    override fun setData(string: String) {
                                                        joinChatRoom(roomId, string)
                                                    }
                                                })
                                                roomLockDialog.show()
                                            } else {
                                                joinChatRoom(roomId, "")
                                            }
                                        }
                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    /**
     * 退出房间
     */
    private fun quiteRoom(uid: String, kick: String, preRoomId: String,newRoomId:String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), mydatabean!!.data.nickname, Uri.parse(mydatabean!!.data.icon))
        val obtain = Message.obtain(preRoomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, preRoomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                                showToastMessage(e!!.message.toString())
                            }

                            override fun onNext(t: CommonBean?) {
                                showToastMessage(t!!.msg)
                                if (t.code == 0) {
                                    IMClient.getInstance().quitChatRoom(preRoomId, null)
                                    RtcClient.getInstance().quitRtcRoom(preRoomId, null)
                                    removeUser(preRoomId, uid, newRoomId)
                                }
                            }

                            override fun onCompleted() {
                            }
                        })
            }

            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                Log.d("tag", p0!!.content.toString())//23409
            }
        });
    }

    fun removeUser(roomId: String, uid: String, newRoomId: String) {
        val map = HashMap<String, String>()
        map.put("room_id", roomId)
        map.put("uid", uid)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .remove_user(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomLock(newRoomId)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, pids: String) =
                HouseItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, param1)
                        putString("pids", pids)
                    }
                }
    }

}
