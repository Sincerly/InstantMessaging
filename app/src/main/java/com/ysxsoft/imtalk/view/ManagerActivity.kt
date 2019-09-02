package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.BlackListBean
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.CircleImageView
import kotlinx.android.synthetic.main.manager_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/13 0013
 */
class ManagerActivity:BaseActivity(){

   companion object {
       fun starManagerActivity(mContext: Context,flag:String,room_id: String){
           val intent = Intent(mContext, ManagerActivity::class.java)
           intent.putExtra("room_id",room_id)
           intent.putExtra("flag",flag)
           mContext.startActivity(intent)
       }
   }

    override fun getLayout(): Int {
        return R.layout.manager_layout
    }
    var flag:String?=null
    var room_id:String?=null
    var type=0;
    lateinit var adapter1: BaseQuickAdapter<BlackListBean.DataBean, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         room_id = intent.getStringExtra("room_id")
         flag = intent.getStringExtra("flag")
        setBackVisibily()
        if ("manage".equals(flag)) {
            setTitle("管理员")
            type=2
        }else{
            setTitle("黑名单")
            type=1
        }
        requestData()
    }

    /**
     * 获取房间黑名单/管理员 - 列表
     */
    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .blackList(SpUtils.getSp(mContext,"uid"),room_id!!,type.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<BlackListBean>{
                    override fun call(t: BlackListBean?) {
                        if (t!!.code==0){
                            adapter1=object :BaseQuickAdapter<BlackListBean.DataBean,BaseViewHolder>(R.layout.manager_item_layout,t.data){
                                override fun convert(helper: BaseViewHolder?, item: BlackListBean.DataBean?) {
                                   ImageLoadUtil.GlideHeadImageLoad(mContext,item!!.icon, helper!!.getView<CircleImageView>(R.id.img_head))
                                    helper.getView<TextView>(R.id.tv_name).setText(item.nickname)
                                    helper.getView<TextView>(R.id.tv_delete).setOnClickListener {
                                    DelData(item.uid,item.id,item.room_id)
                                    }
                                }
                            }
                            recyclerView.layoutManager=LinearLayoutManager(mContext)
                            recyclerView.adapter=adapter1
                        }
                    }
                })

    }

    /**
     * 移除
     */
    private fun DelData(uid:String,id: Int, room_id: String) {
        NetWork.getService(ImpService::class.java)
                .DelBlack(uid,room_id,type.toString(),id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommonBean>{
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            requestData()
                            adapter1.notifyDataSetChanged()
                        }
                    }
                })
    }
}