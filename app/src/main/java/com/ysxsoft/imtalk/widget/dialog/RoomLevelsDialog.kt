package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.WindowManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.RoomLevelsAdapter
import com.ysxsoft.imtalk.com.RoomStarBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseApplication
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.room_level_dialog_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class RoomLevelsDialog : ABSDialog {
    var type = 1
    var roomid:String?=null

    constructor(mContext: Context, room_id: String?) : super(mContext) {
        roomid=room_id
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
        rqeustData(mContext)
    }

    private fun rqeustData(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .roomStar(type.toString(),roomid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<RoomStarBean>{
                    override fun call(t: RoomStarBean?) {
                        if (t!!.code==0){
                            if (t.data.size==1){
                                tv_top1_name.setText(t.data.get(0).nickname)
                                tv_top1_id.setText("ID："+t.data.get(0).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.get(0).icon,img_top1)
                            }

                            if (t.data.size==2) {
                                tv_top1_name.setText(t.data.get(0).nickname)
                                tv_top1_id.setText("ID："+t.data.get(0).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.get(0).icon,img_top1)

                                tv_top2_NO.setText("距离前一名" + t.data.get(1).next_user.toString())
                                tv_top2_name.setText(t.data.get(1).nickname)
                                tv_top2_id.setText("ID：" + t.data.get(1).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.get(1).icon, img_top2)
                            }
                            if (t.data.size==3) {
                                tv_top1_name.setText(t.data.get(0).nickname)
                                tv_top1_id.setText("ID："+t.data.get(0).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.get(0).icon,img_top1)

                                tv_top2_NO.setText("距离前一名" + t.data.get(1).next_user.toString())
                                tv_top2_name.setText(t.data.get(1).nickname)
                                tv_top2_id.setText("ID：" + t.data.get(1).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.get(1).icon, img_top2)

                                tv_top3_name.setText(t.data.get(2).nickname)
                                tv_top3_id.setText("ID：" + t.data.get(2).key_id.toString())
                                ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.get(2).icon, img_top3)
                                tv_top3_NO.setText("距离前一名" + t.data.get(2).next_user.toString())
                            }

                            val adapter = RoomLevelsAdapter(BaseApplication.mContext!!)
                            recyclerView.layoutManager = LinearLayoutManager(BaseApplication.mContext)
                            recyclerView.adapter = adapter
                            adapter.addAll(t.data)

                        }else{
                            ToastUtils.showToast(mContext,t.msg)
                        }
                    }
                })

    }

    override fun initView() {
        tv1.isSelected = true
        tv1.setOnClickListener {
            tv1.isSelected = true
            tv2.isSelected = false
            type = 1
            rqeustData(this.context)
        }
        tv2.setOnClickListener {
            tv1.isSelected = false
            tv2.isSelected = true
            type = 2
            rqeustData(this.context)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.room_level_dialog_layout
    }


}