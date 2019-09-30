package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMwUserBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.GradeIconUtils
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.view.MyDataActivity
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.give_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 * 送礼物
 */
class GiveDialog : ABSDialog {
    var uid:String?=null
    constructor(mContext: Context, userId: String, room_id: String) : super(mContext) {
        uid=userId
        requestData(userId, room_id)
    }

    var bean: RoomMwUserBean.DataBean? = null
    private fun requestData(userId: String, room_id: String) {
        NetWork.getService(ImpService::class.java)
                .roomMwUser(userId, room_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RoomMwUserBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomMwUserBean?) {
                        if (t!!.code == 0) {
                            bean = t.data
                            ImageLoadUtil.GlideHeadImageLoad(this@GiveDialog.context, t.data.icon, img_head)
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID：" + t.data.tt_id)
                            tv_familly.setText("所在家族：" + t.data.fmy_name)
//                            tv_tuhao.setText("豪 " + t.data.user_level)
                            val charmIcon = GradeIconUtils.charmIcon(t.data.ml_level.toInt())
                            img_mei.setImageResource(charmIcon[0])
                            tv_mei.setText(t.data.ml_level)
                            tv_mei.setTextColor(charmIcon[1])
                            val ints = GradeIconUtils.gradeIcon(t.data.user_level.toInt())
                            tv_zs.setText(t.data.user_level)
                            tv_zs.setTextColor(ints[1])
                            img_zs.setImageResource(ints[0])
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

    override fun initView() {
        img_head.setOnClickListener {
//            MyDataActivity.startMyDataActivity(this@GiveDialog.context,uid!!,"")
            if (RoomManager.getInstance().currentRoomInfo==null){
                return@setOnClickListener
            }

            val intent = Intent(this@GiveDialog.context, MyDataActivity::class.java)
            if (uid!!.equals(RoomManager.getInstance().currentRoomInfo!!.roomInfo.uid)){//点击的是房主头像
                intent.putExtra("uid",uid)
                intent.putExtra("is_room","is_room")
            }else{
                intent.putExtra("uid",uid)
            }
            intent.putExtra("myself","")
            intent.putExtra("room","room")
            this@GiveDialog.context.startActivity(intent)

        }

        img_cancle.setOnClickListener {
            dismiss()
        }
        tv_give_gift.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.clickGiveGift(bean!!.uid.toString(),bean!!.nickname)
            }
        }
        tv_private_chat.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.clickPrivateChat()
            }
            val info = UserInfo()
            info.uid = bean!!.uid.toString()
            info.icon = bean!!.icon
            info.nikeName = bean!!.nickname
            info.sex = bean!!.sex
            info.zsl = bean!!.user_level
            info.save()
        }
        tv_give_zb.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.clickGiveZb()
            }
        }

        tv_foucs_on.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.clickFoucsOn()
            }
        }

        tv_setting_manager.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.setManager()
            }
        }

        tv_give_out_room.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.setExit()
            }
        }

        tv_black_list.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.blackList()
            }
        }

        tv_move_manager.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.removeManager()
            }
        }

        tv_bm.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.BmClick()
            }
        }

        tv_btxm.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.BtxmClick()
            }
        }


        tv_scm.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.ScmClick()
            }
        }


    }

    override fun getLayoutResId(): Int {
        return R.layout.give_dialog_layout
    }

    interface GiveClickListener {
        fun clickGiveGift(uid:String,nickname:String)
        fun clickPrivateChat()
        fun clickGiveZb()
        fun clickFoucsOn()
        fun setManager()
        fun removeManager()
        fun setExit()
        fun blackList()
        fun BmClick()
        fun BtxmClick()
        fun ScmClick()
    }

    private var giveClickListener: GiveClickListener? = null;

    fun setGiveClickListener(giveClickListener: GiveClickListener) {
        this.giveClickListener = giveClickListener
    }
}