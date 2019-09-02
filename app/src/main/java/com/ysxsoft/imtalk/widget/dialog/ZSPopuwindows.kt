package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.GiftTimesAdapter
import com.ysxsoft.imtalk.bean.GiftTimesBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.ToastUtils
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class ZSPopuwindows : PopupWindow {
    constructor(mContext: Context, resLayout: Int, view: View) : super(mContext) {
        initView(mContext, resLayout, view)
    }

    private fun initView(mContext: Context, resLayout: Int, view: View) {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val content = LayoutInflater.from(mContext).inflate(resLayout, null, false)
        val recyclerView = content.findViewById<RecyclerView>(R.id.recyclerView)
        contentView = content
        this.showAtLocation(view,Gravity.RIGHT,0,0)

        NetWork.getService(ImpService::class.java)
                .giftTimes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<GiftTimesBean>{
                    override fun call(t: GiftTimesBean?) {
                        if ("0".equals(t!!.code)){
                            val adapter = GiftTimesAdapter(mContext)
                            recyclerView.layoutManager=LinearLayoutManager(mContext)
                            recyclerView.adapter=adapter
                            adapter.addAll(t.data)
                            adapter.setOnGiftTimeListener(object :GiftTimesAdapter.OnGiftTimeListener{
                                override fun onClick(position: Int) {
                                    val times = adapter!!.dataList.get(position).times
                                    val id = adapter!!.dataList.get(position).id.toString()
                                    if (onGiftListener!=null){
                                        onGiftListener!!.giftClick(times,id)
                                    }
                                    dismiss()
                                }
                            })
                        }
                    }
                })
    }

    interface OnGiftListener{
        fun giftClick(times:String,id:String)
    }
    private var onGiftListener: OnGiftListener?=null
    fun setOnGiftListener(onGiftListener: OnGiftListener){
        this.onGiftListener=onGiftListener
    }
}