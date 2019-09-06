package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.ViewGroup
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.SmilDialogAdapter
import com.ysxsoft.imtalk.bean.FaceListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.smile_dialog_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/5 0005
 */
class SmilDialog:ABSDialog{
    override fun initView() {

    }

    override fun getLayoutResId(): Int {
        return R.layout.smile_dialog_layout
    }

    constructor(mContext: Context):super(mContext){
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width=ViewGroup.LayoutParams.MATCH_PARENT
        params.height=ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes=params
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .FaceList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<FaceListBean>{
                    override fun call(t: FaceListBean?) {
                        if (t!!.code==0){
                            val data = t.data
                            val adapter = SmilDialogAdapter(this@SmilDialog.context)
                            val manager = GridLayoutManager(this@SmilDialog.context, 6)
                            manager.orientation=GridLayoutManager.VERTICAL
                            recyclerView.layoutManager=manager
                            recyclerView.adapter=adapter
                            adapter.addAll(data)
                            adapter.setOnSmileListener(object :SmilDialogAdapter.OnSmileListener{
                                override fun onClick(position: Int,url:String) {
                                    if(onDialogListener!=null){
                                        onDialogListener!!.onClick(position,url)
                                    }
                                   //ToastUtils.showToast(this@SmilDialog.context,url)
                                }
                            })
                        }
                    }
                })
    }

    interface OnSmileDialogListener {
        fun onClick(position: Int,url:String)
    }

    private var onDialogListener: OnSmileDialogListener? = null
    fun setOnDialogListener(onSmileListener: OnSmileDialogListener) {
        this.onDialogListener = onSmileListener
    }

}