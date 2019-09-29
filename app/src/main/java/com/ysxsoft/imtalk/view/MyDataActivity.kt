package com.ysxsoft.imtalk.view

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.R.mipmap.myself
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.FouceOnBean
import com.ysxsoft.imtalk.bean.RoomLockBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.fragment.*
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.ReportDialog
import com.ysxsoft.imtalk.widget.dialog.RoomLockDialog
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.my_data_layout.*
import kotlinx.android.synthetic.main.w_translation_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDataActivity : BaseActivity() {

    companion object {
        fun startMyDataActivity(mContext: Context, uid: String, myself: String) {
            val intent = Intent(mContext, MyDataActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("myself", myself)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.my_data_layout
    }

    private var tagP = "0"
    var uid: String? = null
    var myself: String? = null
    var room: String? = null
    var is_room: String? = null
    var bean: UserInfoBean.DataBean? = null
    var mybean: UserInfoBean.DataBean? = null
    var data: Int? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = intent.getStringExtra("uid")
        myself = intent.getStringExtra("myself")
        room = intent.getStringExtra("room")
        is_room = intent.getStringExtra("is_room")
        if ("myself".equals(myself)) {
            tv_za_ta.visibility = View.GONE
            tv_t_room.visibility = View.GONE
            img_right.setImageResource(R.mipmap.img_edittext)
            img_right.visibility = View.VISIBLE
            setTitle("我的资料")
            img_right.setOnClickListener {
                startActivity(PersonDataActivity::class.java)
            }

            ll_fouce.setOnClickListener {
                FouceOnActivity.startFouceOnActivity(mContext, uid!!)
            }

            ll_fance.setOnClickListener {
                FansActivity.startFansActivity(mContext, uid!!)
            }
            llshow.visibility = View.GONE
        } else {
            tv_za_ta.visibility = View.VISIBLE
            tv_t_room.visibility = View.VISIBLE
            img_right.setImageResource(R.mipmap.img_w_more)
            img_right.visibility = View.VISIBLE
            setTitle("TA的资料")

            img_right.setOnClickListener {
                ReportDialog(mContext, uid!!).show()
            }
            llshow.visibility = View.VISIBLE
        }
        requestMySelfData()
        requestData()
        setBackVisibily()
        initStatusBar(topView)
        setLightStatusBar(false)
    }

    override fun onResume() {
        super.onResume()
        requestMySelfData()
        requestData()
        fouceData()
    }

    private fun fouceData() {
        NetWork.getService(ImpService::class.java)
                .fans_status(SpUtils.getSp(mContext, "uid"), uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FouceOnBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("MyDataGiftFragment", e!!.message.toString())
                    }

                    override fun onNext(t: FouceOnBean?) {
                        if (t!!.code == 0) {
                            data = t.data
                            if (t.data == 1) {//未关注
                                img_fouce.setImageResource(R.mipmap.img_w_add)
                                tv_fouce.setText("关注")
                            } else {//已关注  取消
                                img_fouce.setImageResource(R.mipmap.img_w_dui)
                                tv_fouce.setText("已关注")
                            }
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    private fun requestMySelfData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(AuthManager.getInstance().currentUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            mybean = t.data
                            if (!TextUtils.isEmpty(t.data.user_ts_pic)){
                               ImageLoadUtil.GlideGoodsImageLoad(mContext,t.data.user_ts_pic,img_head_wear)
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            bean = t.data
                            initView()
                            if (!TextUtils.isEmpty(t.data.user_ts_pic)){
                                ImageLoadUtil.GlideGoodsImageLoad(mContext,t.data.user_ts_pic,img_head_wear)
                            }
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID:" + t.data.tt_id)
                            tv_fance.setText(t.data.fans.toString())
                            tv_foucs.setText(t.data.gzrs.toString())

                            val charmIcon = GradeIconUtils.charmIcon(t.data.charm_level)
                            img_mei.setImageResource(charmIcon[0])
                            tv_mei.setText(t.data.charm_level.toString())
                            tv_mei.setTextColor(charmIcon[1])
                            val ints = GradeIconUtils.gradeIcon(t.data.user_level)
                            tv_zs.setText(t.data.user_level.toString())
                            img_zs.setImageResource(ints[0])
                            tv_zs.setTextColor(ints[1])
                            when (t.data.sex) {
                                "1" -> {
                                    img_sex.setImageResource(R.mipmap.img_boy)
                                }
                                "2" -> {
                                    img_sex.setImageResource(R.mipmap.img_girl)
                                }
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {
        replaceFragment()
        if ("myself".equals(myself)) {
            llshow.visibility = View.GONE
        } else {
            llshow.visibility = View.VISIBLE
            tv_send_car.visibility = View.GONE
        }

        tv_data.setOnClickListener {
            if (tv_data.isSelected) {
                return@setOnClickListener
            }
            tagP = "0"
            replaceFragment()
            if ("myself".equals(myself)) {
                llshow.visibility = View.GONE
            } else {
                llshow.visibility = View.VISIBLE
                tv_send_car.visibility = View.GONE
            }

        }

        tv_gift.setOnClickListener {
            if (tv_gift.isSelected) {
                return@setOnClickListener
            }
            tagP = "1"
            replaceFragment()
            if ("myself".equals(myself)) {
                llshow.visibility = View.GONE
            } else {
                llshow.visibility = View.VISIBLE
                tv_send_car.visibility = View.GONE
            }
        }

        tv_car.setOnClickListener {
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            tagP = "2"
            replaceFragment()
            if ("myself".equals(myself)) {
                llshow.visibility = View.GONE
            } else {
                llshow.visibility = View.VISIBLE
                tv_send_car.visibility = View.VISIBLE
            }
        }

        tv_za_ta.setOnClickListener {
            //Ta 所在的房间
            if (TextUtils.equals("room", room)) {
                showToastMessage("已经和对方在同一个房间")
                return@setOnClickListener
            }
            if (TextUtils.equals("SearchActivity", myself)) {
                if (bean != null) {
                    roomLock(bean!!.now_roomId.toString())
                }
            } else {
                joinChatRoom(bean!!.now_roomId, "")
            }
        }

        tv_t_room.setOnClickListener {
            //Ta创建的房间
            if (TextUtils.equals("is_room", is_room)) {
                finish()
                return@setOnClickListener
            }
            if (TextUtils.equals("SearchActivity", myself)) {
                if (bean!!.roomId != null) {
                    roomLock(bean!!.roomId.toString())
                } else {
                    showToastMessage("Ta没有房间")
                }
            } else {
                if (!TextUtils.isEmpty(mybean!!.now_roomId)){
                    if (!TextUtils.isEmpty(bean!!.roomId)){
                        quiteRoom(AuthManager.getInstance().currentUserId, "1",mybean!!.now_roomId)
                    }else{
                        showToastMessage("Ta没有房间")
                    }
                }else{
                    if (!TextUtils.isEmpty(bean!!.roomId)){
                        roomLock(bean!!.roomId.toString())
                    }else{
                        showToastMessage("Ta没有房间")
                    }
                }

//                if (!TextUtils.isEmpty(mybean!!.now_roomId) && !TextUtils.isEmpty(bean!!.roomId)) {
//                    roomLock(bean!!.roomId.toString())
//                } else {
//                    if (TextUtils.isEmpty(bean!!.roomId)) {
//                        showToastMessage("Ta没有房间")
//                        return@setOnClickListener
//                    }
//                    if (!TextUtils.isEmpty(mybean!!.now_roomId)) {
//                        quiteRoom(AuthManager.getInstance().currentUserId, "1")
//                    }
//                }
            }
        }

        tv_msg.setOnClickListener {
            //私信
            RongIM.getInstance().startPrivateChat(mContext, uid, bean!!.nickname);
        }
        tv_send_car.setOnClickListener {
            //送座驾
            RongIM.getInstance().startPrivateChat(mContext, uid, bean!!.nickname);
        }

        ll_add_fouce.setOnClickListener {
            //关注
            if (data == 1) {
                FocusOnData(AuthManager.getInstance().currentUserId, uid!!, "1")
            } else {
                FocusOnData(AuthManager.getInstance().currentUserId, uid!!, "2")
            }
        }

        NscrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(p0: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                /**
                 * 第一个参数NestedScrollView v:是NestedScrollView的对象
                 *第二个参数:scrollX是目前的（滑动后）的X轴坐标
                第三个参数:ScrollY是目前的（滑动后）的Y轴坐标
                第四个参数:oldScrollX是之前的（滑动前）的X轴坐标
                第五个参数:oldScrollY是之前的（滑动前）的Y轴坐标
                 */
                Log.e("tag","scrollY===="+scrollY)
                Log.e("tag","oldScrollY====>>>>>>>>>>>"+oldScrollY)
                if (scrollY - oldScrollY > AppUtil.dip2px(mContext, 0)) {
                    llshow.visibility = View.GONE
                } else {
                    llshow.visibility = View.VISIBLE
                }
                if ("myself".equals(myself)) {
                    llshow.visibility = View.GONE
                }
            }
        })
    }

    /**
     * 关注
     */
    private fun FocusOnData(sp: String, userId: String, s: String) {
        val map = HashMap<String, String>()
        map.put("uid", sp)
        map.put("fs_id", userId)
        map.put("flag", s)
//        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .fans(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
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

    /**
     * 我 退出房间
     */
    private fun quiteRoom(uid: String, kick: String,newRoomId:String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(AuthManager.getInstance().currentUserId, mybean!!.nickname, Uri.parse(mybean!!.icon))
        val obtain = Message.obtain(newRoomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, newRoomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: CommonBean?) {
                                showToastMessage(t!!.msg)
                                if (t.code == 0) {
                                    IMClient.getInstance().quitChatRoom(newRoomId, null)
                                    RtcClient.getInstance().quitRtcRoom(newRoomId, null)
                                    roomLock(bean!!.roomId.toString())
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
                        Log.d("tag++++++", e!!.message.toString())
                    }

                    override fun onNext(t: RoomLockBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            RoomManager.getInstance().getRoomDetailInfo1(roomId, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(result: DetailRoomInfo?) {
                                    if (result != null) {
                                        if (AuthManager.getInstance().currentUserId.equals(result.roomInfo.uid)) {
                                            joinChatRoom(roomId, "")
                                        } else {
                                            if (t.data == 1) {
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
                                    Log.d("tag>>>>>>>>>>>>", errorCode.toString())
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    /**
     * 加入他的房间
     */
    fun joinChatRoom(roomId: String, lock_pwd: String) {
        sendBroadcast(Intent("WINDOW"))
        RoomManager.getInstance().joinRoom(AuthManager.getInstance().currentUserId, roomId, lock_pwd, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                val message = RoomMemberChangedMessage()
                message.setCmd(1)
                val carName = SharedPreferencesUtils.getCarName(mContext)
                val carPic = SharedPreferencesUtils.getCarPic(mContext)
                message.carName = carName//座驾名称
                message.carPic = carPic//座驾图片
                message.targetUserId = SpUtils.getSp(mContext, "uid")
                message.targetPosition = -1
                message.userInfo = io.rong.imlib.model.UserInfo(AuthManager.getInstance().currentUserId, mybean!!.nickname, Uri.parse(mybean!!.icon))
                val obtain = Message.obtain(roomId, Conversation.ConversationType.CHATROOM, message)
                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                    override fun onAttached(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                        if ("room".equals(room)){
                            sendBroadcast(Intent("FINSH"))
                        }
                    }

                    override fun onSuccess(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                        ChatRoomActivity.starChatRoomActivity(mContext, roomId, bean!!.nickname, bean!!.icon, "")
                        finish()
                    }

                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                        Log.d("tag", p0!!.content.toString())
                    }
                });
            }

            override fun onFail(errorCode: Int) {
                Log.d("tag>>>>>>>>>>>>", errorCode.toString())
            }
        })
    }

    private var currentFragment: BaseFragment? = null

    private fun replaceFragment() {
        tv_data.isSelected = tagP.equals("0")
        tv_gift.isSelected = tagP.equals("1")
        tv_car.isSelected = tagP.equals("2")

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> {
                    currentFragment = MyDataFragment()
                    var bundle = Bundle();
                    bundle.putString("uid", uid);
                    bundle.putString("nikeName", bean!!.nickname);
                    bundle.putString("myself", myself);
                    currentFragment!!.setArguments(bundle)
                }
                "1" -> {
                    currentFragment = MyDataGiftFragment()
                    var bundle = Bundle();
                    bundle.putString("uid", uid);
                    bundle.putString("nikeName", bean!!.nickname);
                    bundle.putString("myself", myself);
                    currentFragment!!.setArguments(bundle)
                }
                "2" -> {
                    currentFragment = MyDataCarFragment()
                    var bundle = Bundle();
                    bundle.putString("uid", uid);
                    bundle.putString("nikeName", bean!!.nickname);
                    bundle.putString("myself", myself);
                    currentFragment!!.setArguments(bundle)
                }
            }
            currentFragment!!.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }

    }
}