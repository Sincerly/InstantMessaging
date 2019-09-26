package com.ysxsoft.imtalk.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgTableBean
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import cn.rongcloud.rtc.utils.BuildInfo
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgVpAdapter
import com.ysxsoft.imtalk.bean.HomeTableBean
import com.ysxsoft.imtalk.bean.RealInfoBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.model.CreateRoomResult
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.ChatRoomActivity
import com.ysxsoft.imtalk.view.SearchActivity
import com.ysxsoft.imtalk.view.SmrzActivity
import kotlinx.android.synthetic.main.fragment_house.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class HouseFragment : BaseFragment() {

    var titles: MutableList<HomeTableBean.DataBean>? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_house
    }
    var mydatabean: UserInfoBean? = null
    override fun initUi() {
        requestMyData()
        tv_title.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }
        topView.post {
            val params: ViewGroup.LayoutParams = topView.layoutParams
            params.height = getStateBar()
            topView.layoutParams = params
        }
        img_right.setOnClickListener {
            activity!!.sendBroadcast(Intent("WINDOW"))
            getRealName()
        }
        requestTable()
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
                if (!checkPermissions()) return
                // 标记正在进入房间
                if (!TextUtils.isEmpty(result!!.roomInfo.room_id))
                    joinChatRoom(result.roomInfo.room_id, "")
            }

            override fun onFail(errorCode: Int) {
                Log.d("tag=errorCode=>>", errorCode.toString())
            }
        })
    }

    private fun joinChatRoom(roomId: String, isCreate: String) {
        showToastMessage(R.string.toast_joining_room)
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, isCreate, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                ChatRoomActivity.starChatRoomActivity(mContext, roomId, mydatabean!!.data.nickname, mydatabean!!.data.icon, "")
            }

            override fun onFail(errorCode: Int) {

            }
        })
    }

    private fun requestTable() {
        NetWork.getService(ImpService::class.java)
                .homeTable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeTableBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("HouseFragment", e!!.message.toString())
                    }

                    override fun onNext(t: HomeTableBean?) {
                        if (t!!.code == 0) {
                            titles = t.data
                            initAdapter()
                        }
                    }

                    override fun onCompleted() {

                    }
                })
    }

    private fun initAdapter() {
        val informfragments = ArrayList<FgTableBean<HouseItemFragment>>()
        for (i in 0 until titles!!.size) {
            tabLayoutHouse.addTab(titles!!.get(i).pname)
            informfragments.add(FgTableBean(HouseItemFragment.newInstance(i, titles!!.get(i).pids.toString()), titles!!.get(i).pids.toString(), i))
        }
        val fgVpAdapter = FgVpAdapter(childFragmentManager, informfragments)
        viewPagerHouse.adapter = fgVpAdapter
        viewPagerHouse.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutHouse.getTabLayout()))
        tabLayoutHouse.setupWithViewPager(viewPagerHouse)
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