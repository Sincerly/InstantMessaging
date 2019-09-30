package com.ysxsoft.imtalk.utils

import android.util.Log
import com.ysxsoft.imtalk.bean.GroupIdBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.pallobby.PalLobbyGradeListener
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 交友大厅等级
 */
object PalLobbyGrade {

    var groupId : String = ""

    fun getGroupId(listener : PalLobbyGradeListener?){
        if (groupId.isNotEmpty()){
            listener?.getGroupId(groupId)
        }else{
            requestGroupData(listener)
        }
    }

    private fun requestGroupData(listener : PalLobbyGradeListener?) {
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
//                        ToastUtils.showToast(MyApplication.mcontext, "获取交友大厅信息失败！！")
                    }

                    override fun onNext(t: GroupIdBean?) {
                        if (t!!.code == 0) {
                            groupId = t.data.groupId
                            listener?.getGroupId(groupId)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

}
