package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
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
    var mydatagiftfragment = MyDataGiftFragment()
    var mydatacarfragment = MyDataCarFragment()
    var mydatafragment = MyDataFragment()
    var bean: UserInfoBean.DataBean? = null
    var mybean: UserInfoBean.DataBean? = null
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
        } else {
            tv_za_ta.visibility = View.VISIBLE
            tv_t_room.visibility = View.VISIBLE
            img_right.setImageResource(R.mipmap.img_w_more)
            img_right.visibility = View.VISIBLE
            setTitle("TA的资料")

            img_right.setOnClickListener {
                ReportDialog(mContext,uid!!).show()
            }

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
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID:" + t.data.tt_id)
                            tv_fance.setText(t.data.fans.toString())
                            tv_foucs.setText(t.data.gzrs.toString())
                            tv_zs.setText(t.data.user_level.toString())
                            tv_m.setText("魅" + t.data.charm_level.toString())
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


        ll_fouce.setOnClickListener {
            FouceOnActivity.startFouceOnActivity(mContext, uid!!)
        }
        ll_fance.setOnClickListener {
            FansActivity.startFansActivity(mContext, uid!!)
        }

        val bundle = Bundle();
        bundle.putString("uid", uid);
        bundle.putString("nikeName",bean!!.nickname);
        bundle.putString("myself", myself);
        mydatafragment.setArguments(bundle);//数据传递到fragment中
        replaceFragment()
        tv_data.setOnClickListener {
            if (tv_data.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("nikeName", bean!!.nickname);
            bundle.putString("myself", myself);
            mydatafragment.setArguments(bundle);//数据传递到fragment中
            tagP = "0"
            replaceFragment()
        }

        tv_gift.setOnClickListener {
            if (tv_gift.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("nikeName", bean!!.nickname);
            bundle.putString("myself", myself);
            mydatagiftfragment.setArguments(bundle);//数据传递到fragment中

            tagP = "1"
            replaceFragment()
        }

        tv_car.setOnClickListener {
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("nikeName", bean!!.nickname);
            bundle.putString("myself", myself);
            mydatacarfragment.setArguments(bundle);//数据传递到fragment中
            tagP = "2"
            replaceFragment()
        }

        tv_za_ta.setOnClickListener {
            //Ta 所在的房间
            if (TextUtils.equals("room", room)) {
                showToastMessage("已经和对方在同一个房间")
                return@setOnClickListener
            }
            if (TextUtils.equals("SearchActivity", myself)) {
                roomLock(bean!!.now_roomId.toString())
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
                if (!TextUtils.isEmpty(mybean!!.now_roomId)&&!TextUtils.isEmpty(bean!!.roomId)){
                    roomLock(bean!!.roomId.toString())
                }else{
                    if (TextUtils.isEmpty(bean!!.roomId)){
                        showToastMessage("Ta没有房间")
                        return@setOnClickListener
                    }
                    if (!TextUtils.isEmpty(mybean!!.now_roomId)){
                        quiteRoom(AuthManager.getInstance().currentUserId, "1")
                    }
                }
            }
        }
    }

    /**
     * 我 退出房间
     */
    private fun quiteRoom(uid: String, kick: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), mybean!!.nickname, Uri.parse(mybean!!.icon))
        val obtain = Message.obtain(bean!!.now_roomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, bean!!.now_roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: CommonBean?) {
                                showToastMessage(t!!.msg)
                                if (t.code == 0) {
                                    IMClient.getInstance().quitChatRoom(bean!!.now_roomId, null)
                                    RtcClient.getInstance().quitRtcRoom(bean!!.now_roomId, null)
//                                    joinChatRoom(bean!!.roomId)
                                    roomLock(bean!!.roomId.toString())
                                    finish()
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
                    }

                    override fun onNext(t: RoomLockBean?) {
                        if (t!!.code == 0) {
                            RoomManager.getInstance().getRoomDetailInfo1(roomId, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(result: DetailRoomInfo?) {
                                    if (result != null) {
                                        if (AuthManager.getInstance().currentUserId.equals(result.roomInfo.uid)) {
                                            joinChatRoom(bean!!.roomId, "")
                                        } else {
                                            var roomLock = t.data
                                            if (roomLock == 1) {
                                                val roomLockDialog = RoomLockDialog(mContext)
                                                roomLockDialog.setEdClickListener(object : RoomLockDialog.EdClickListener {
                                                    override fun setData(string: String) {
                                                        joinChatRoom(bean!!.roomId, string)
                                                    }
                                                })
                                                roomLockDialog.show()
                                            } else {
                                                joinChatRoom(bean!!.roomId, "")
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
     * 加入他的房间
     */
    fun joinChatRoom(roomId: String, lock_pwd: String) {
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, lock_pwd, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                val message = RoomMemberChangedMessage()
                message.setCmd(1)
                val carName=SharedPreferencesUtils.getCarName(mContext)
                val carPic=SharedPreferencesUtils.getCarPic(mContext)
                message.carName=carName//座驾名称
                message.carPic=carPic//座驾图片
                message.targetUserId = SpUtils.getSp(mContext, "uid")
                message.targetPosition = -1
                message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), mybean!!.nickname, Uri.parse(mybean!!.icon))
                val obtain = Message.obtain(result!!.roomInfo.room_id, Conversation.ConversationType.CHATROOM, message)
                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                    override fun onAttached(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                    }

                    override fun onSuccess(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                        ChatRoomActivity.starChatRoomActivity(mContext, roomId, bean!!.nickname, bean!!.icon)
                    }

                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                        Log.d("tag", p0!!.content.toString())
                    }
                });
            }

            override fun onFail(errorCode: Int) {

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
                "0" -> currentFragment = mydatafragment
                "1" -> currentFragment = mydatagiftfragment
                "2" -> currentFragment = mydatacarfragment
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }

    }
}