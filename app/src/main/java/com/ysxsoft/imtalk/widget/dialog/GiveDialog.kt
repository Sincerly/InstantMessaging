package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMwUserBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
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
    constructor(mContext: Context, userId: String, room_id: String) : super(mContext) {
        requestData(userId, room_id)
    }
    var bean:RoomMwUserBean.DataBean?=null
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
                            tv_tuhao.setText("豪 "+t.data.user_level)
                            tv_mei.setText("魅 " + t.data.ml_level)
                            tv_zs.setText(t.data.user_level)
                            when(t.data.sex){
                                "1"->{
                                    img_sex.setImageResource(R.mipmap.img_boy)
                                }
                                 "2"->{
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
        img_cancle.setOnClickListener {
            dismiss()
        }
        tv_give_gift.setOnClickListener {
            dismiss()
            if (giveClickListener != null) {
                giveClickListener!!.clickGiveGift()
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



    }

    override fun getLayoutResId(): Int {
        return R.layout.give_dialog_layout
    }

    interface GiveClickListener {
        fun clickGiveGift()
        fun clickPrivateChat()
        fun clickGiveZb()
        fun clickFoucsOn()
        fun setManager()
        fun removeManager()
        fun setExit()
        fun blackList()
    }

    private var giveClickListener: GiveClickListener? = null;

    fun setGiveClickListener(giveClickListener: GiveClickListener) {
        this.giveClickListener = giveClickListener
    }
}