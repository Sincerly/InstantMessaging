package com.ysxsoft.imtalk.fragment

import android.app.Activity
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.rongcloud.rtc.utils.BuildInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.listener.OnBannerListener
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.BannerAdapter
import com.ysxsoft.imtalk.appservice.FloatingDisplayService
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.model.CreateRoomResult
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.com.GallerySnapHelper
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.*
import com.ysxsoft.imtalk.widget.CircleImageView
import com.ysxsoft.imtalk.widget.UniversalItemDecoration
import com.ysxsoft.imtalk.widget.dialog.RoomLockDialog
import kotlinx.android.synthetic.main.fm_home.*
import org.w3c.dom.Text
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.*

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class HomeFragment : BaseFragment(), OnBannerListener {
    override fun OnBannerClick(position: Int) {
//        val banner_id = lists!!.get(position).id.toString()
//        val intent = Intent(mContext, BannerDetailActivity::class.java)
//        intent.putExtra("banner_id", banner_id)
//        startActivity(intent)
    }

    lateinit var adapter1: BaseQuickAdapter<HomeRoomListBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapter2: BaseQuickAdapter<HomeRoomListBean.DataBean.RoomListBean, BaseViewHolder>
    var mydatabean: UserInfoBean? = null

    override fun getLayoutResId(): Int {
        return R.layout.fm_home
    }


    override fun initUi() {
        relTitle.post {
            val params = relTitle.layoutParams as RelativeLayout.LayoutParams
            params.setMargins(0, getStateBar(), 0, 0)
            relTitle.layoutParams = params
        }
        requestData()
        HomeData()
        requestHLData()
        requestMyData()
        initClickListernr()
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
                            mydatabean = t
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun HomeData() {
        NetWork.getService(ImpService::class.java)
                .HomeCateList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeCateListBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", "HomeData==" + e!!.message.toString())
                    }

                    override fun onNext(t: HomeCateListBean?) {
                        if (t!!.code == 0) {
                            val pid = t.data.get(0).pids
                            tv_title.setText(t.data.get(0).pname)
                            ListData(pid)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    //热门推荐
    private fun ListData(pid: String?) {
        NetWork.getService(ImpService::class.java)
                .homeRoomList(pid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeRoomListBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: HomeRoomListBean?) {
                        if (t!!.code == 0) {
                            tv1.setText(t.data.get(0).cname)
                            tv2.setText(t.data.get(1).cname)
                            roomLists0 = t.data.get(0).roomList
                            roomLists1 = t.data.get(1).roomList

                            adapter1 = object : BaseQuickAdapter<HomeRoomListBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_home_recommend, roomLists0) {
                                override fun convert(helper: BaseViewHolder?, item: HomeRoomListBean.DataBean.RoomListBean?) {
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item!!.icon, helper!!.getView<ImageView>(R.id.ivPhoto))

                                    if (TextUtils.isEmpty(item.label_name)) {
                                        helper.getView<TextView>(R.id.tvContent).setText("#" + "暂无" + "  " + item.room_name)
                                    } else {
                                        helper.getView<TextView>(R.id.tvContent).setText("#" + item.label_name + "  " + item.room_name)
                                    }

                                    helper.itemView.setOnClickListener {
                                        //                                        ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                                        roomLock(item.room_id.toString())
                                    }
                                }
                            }

                            recyclerView1.addItemDecoration(object : UniversalItemDecoration() {
                                override fun getItemOffsets(position: Int): Decoration {
                                    val decoration = ColorDecoration(R.color.white)
                                    decoration.left = AppUtil.dip2px(mContext, if (position % 2 == 0) 10 else 5)
                                    decoration.right = AppUtil.dip2px(mContext, if (position % 2 == 0) 5 else 10)
                                    return decoration
                                }
                            })
                            recyclerView1.layoutManager = GridLayoutManager(mContext, 2) as RecyclerView.LayoutManager?
                            recyclerView1.adapter = adapter1
                            adapter2 = object : BaseQuickAdapter<HomeRoomListBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_room, roomLists1) {
                                override fun convert(helper: BaseViewHolder?, item: HomeRoomListBean.DataBean.RoomListBean?) {
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item!!.icon, helper!!.getView<CircleImageView>(R.id.ivAvatar))
                                    helper.getView<TextView>(R.id.tv_name).setText(item.room_name)
                                    if (TextUtils.isEmpty(item.label_name)) {
                                        helper.getView<TextView>(R.id.tv_Tag).text = "#" + "暂无"
                                    } else {
                                        helper.getView<TextView>(R.id.tv_Tag).text = "#" + item.label_name
                                    }
                                    if (TextUtils.isEmpty(item.memCount)) {
                                        helper.getView<TextView>(R.id.tv_Online).text = "0" + "人在线"
                                    } else {
                                        helper.getView<TextView>(R.id.tv_Online).text = item.memCount + "人在线"
                                    }
                                    helper.itemView.setOnClickListener {
                                        //                                        ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                                        roomLock(item.room_id.toString())
                                    }
                                }
                            }
                            recyclerView2.layoutManager = LinearLayoutManager(mContext)
                            recyclerView2.adapter = adapter2
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    var type = 1
    var urls = ArrayList<String>()
    var lists: MutableList<BannerBean.DataBean>? = null
    var roomLists0: MutableList<HomeRoomListBean.DataBean.RoomListBean>? = null
    var roomLists1: MutableList<HomeRoomListBean.DataBean.RoomListBean>? = null
    @Volatile
    private var isJoiningRoom = false // 是否正有加入房间操作
    var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as (FloatingDisplayService.MyBinder)
            startActivity(ChatRoomActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        // 判断是否有手机运行权限进行语音聊天
        checkPermissions()
        /*
         * 当成功加入房间后，再次回到主界面时才可以再加入其他房间
         * 防止多次加入房间问题
         */
        isJoiningRoom = false
    }

    var room_id: String? = null
    //海聊
    private fun requestHLData() {
        NetWork.getService(ImpService::class.java)
                .homeroomId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeHLBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage("嗨聊==" + e!!.message.toString())
                    }

                    override fun onNext(t: HomeHLBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideGoodsImageLoad(mContext, t.data.pic, ivHai)
                            room_id = t.data.room_id.toString()
                            tvtitle.setText(t.data.title)
                            tv_content.setText(t.data.desc)
                        }
                    }

                    override fun onCompleted() {

                    }
                })
    }

    /**
     * 获取轮播图数据
     */
    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .HomeBanner(type.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BannerBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: BannerBean?) {
                        if (t!!.code == 0) {
                            lists = t.data
                            for (bean in t.data) {
                                urls.add(bean.pic)
                            }
//                            banner.setImages(urls)
//                                    .setImageLoader(GlideImageLoader())
//                                    .setOnBannerListener(this@HomeFragment)
//                                    .start()

                            val adapter = BannerAdapter(mContext, t.data)
                            recyclerView.adapter = adapter
                            val manager = LinearLayoutManager(mContext)
                            manager.orientation = LinearLayoutManager.HORIZONTAL
                            val snapHelper = GallerySnapHelper()
                            snapHelper.attachToRecyclerView(recyclerView)
//                            val manager = LooperLayoutManager()
//                            manager.setLooperEnable(true)
                            recyclerView.layoutManager = manager

                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initClickListernr() {
        //签到
        ivSign.visibility=View.GONE
        ivSign.setOnClickListener {
            startActivity(QDActivity::class.java)
//            val dialog = DatePickerDialog(mContext)
//            dialog.setClickDatePicker(object : DatePickerDialog.ClickDatePicker {
//                override fun datePicker(date: String) {
//                    showToastMessage(date)
//                }
//            })
//            dialog.show()
        }    //新建房间
        ivRoom.setOnClickListener {
            getRealName()
        }
        //搜索
        ivSearch.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }
        //嗨爆聊天
        cardView.setOnClickListener {
            //            ChatRoomActivity.starChatRoomActivity(mContext, room_id!!)
            joinChatRoom(room_id!!, "")
        }
    }

    private fun getRealName() {
        NetWork.getService(ImpService::class.java)
                .getRealInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<RealInfoBean> {
                    override fun call(t: RealInfoBean?) {
                        if ("0".equals(t!!.code)) {
                            when (t.data.is_real) {
                                0 -> {
                                    showToastMessage("未实名认证")
                                    startActivity(SmrzActivity::class.java)
                                }
                                1 -> {
                                    CreateRoom()
                                }

                            }
                        }
                    }
                })
    }

    /**
     * 获取房间id
     */
    private fun CreateRoom() {
        RoomManager.getInstance().createRoom(SpUtils.getSp(mContext, "uid"), object : ResultCallback<CreateRoomResult> {
            override fun onSuccess(result: CreateRoomResult?) {
                if (result == null) return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!checkPermissions()) return
                }
                // 标记正在进入房间
                isJoiningRoom = true
                if (!TextUtils.isEmpty(result.roomInfo.room_id))
                    joinChatRoom(result.roomInfo.room_id, "")
            }

            override fun onFail(errorCode: Int) {

            }
        })
    }

    private fun joinChatRoom(roomId: String, isCreate: String) {
        showToastMessage(R.string.toast_joining_room)
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, isCreate, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                ChatRoomActivity.starChatRoomActivity(mContext, roomId, mydatabean!!.data.nickname, mydatabean!!.data.icon)
            }

            override fun onFail(errorCode: Int) {

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
     * 校验语音聊天的权限
     */
    private fun checkPermissions(): Boolean {
        val unGrantedPermissions = java.util.ArrayList<String>()
        for (permission in BuildInfo.MANDATORY_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mContext, permission) !== PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(permission)
            }
        }
        if (unGrantedPermissions.size == 0) {//已经获得了所有权限
            return true
        } else {//部分权限未获得，重新请求获取权限
            val array = arrayOfNulls<String>(unGrantedPermissions.size)
            ActivityCompat.requestPermissions(mContext as Activity, unGrantedPermissions.toTypedArray(), 0)
            return false
        }
    }
}