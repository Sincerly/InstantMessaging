package com.ysxsoft.imtalk.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.RoomBackGroundAdapter
import com.ysxsoft.imtalk.bean.RoomBgBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.manager_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/30 0030
 */
class RoomBackGroundActivity:BaseActivity(){
    override fun getLayout(): Int {
        return R.layout.room_background_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv_title_right.visibility=View.VISIBLE
        tv_title_right.setText("完成")
        setBackVisibily()
        setTitle("房间背景")
        requestData()
        tv_title_right.setOnClickListener {
            val intent = Intent()
            intent.putExtra("img_id",img_id)
            intent.putExtra("img_url",img_url)
            setResult(1,intent)
            finish()
        }
    }
    var img_id:String?=null
    var img_url:String?=null
    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .RoomBg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<RoomBgBean>{
                    override fun call(t: RoomBgBean?) {
                        if (t!!.code==0){
                            val adapter = RoomBackGroundAdapter(mContext)
                            recyclerView.layoutManager=GridLayoutManager(mContext,3)
                            recyclerView.adapter=adapter
                            adapter.addAll(t.data)
                            adapter.setOnItemClickListener(object :RoomBackGroundAdapter.OnItemClickListener{
                                override fun OnClick(position: Int) {
                                    img_url = adapter.dataList.get(position).bg_url
                                    img_id = adapter.dataList.get(position).id
                                    adapter.setSelect(position)
                                }
                            })
                        }
                    }
                })
    }

}