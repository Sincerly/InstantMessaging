package com.ysxsoft.imtalk.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.id_flowlayout
import com.ysxsoft.imtalk.bean.TabListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.widget.flowlayout.FlowLayout
import com.ysxsoft.imtalk.widget.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.room_tag_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/13 0013
 */
class RoomTagActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.room_tag_layout
    }

//    var datas = arrayListOf("聊天", "音乐", "吃鸡", "男神", "女神", "真心话", "王者")
    var datas :MutableList<TabListBean.DataBean>?=null
    var tagName: String? = null
    var tabid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        requestData()
        initView()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .TabList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<TabListBean>{
                    override fun call(t: TabListBean?) {
                        if (t!!.code==0){
                             datas = t.data
                            id_flowlayout.adapter = object : TagAdapter<TabListBean.DataBean>(datas) {
                                override fun getView(parent: FlowLayout?, position: Int, t: TabListBean.DataBean?): View {
                                    val tv = LayoutInflater.from(mContext).inflate(R.layout.flow_item_layout, parent, false) as TextView
                                    tv.setText(t!!.label_name)
                                    return tv
                                }
                            }

                            id_flowlayout.setOnTagClickListener { view, position, parent ->
                                tagName = datas!!.get(position).label_name
                                 tabid = datas!!.get(position).id.toString()
                                return@setOnTagClickListener true
                            }
                        }
                    }
                })

    }

    private fun initView() {
        img_close.setOnClickListener {
            finish()
        }

        tv_ok.setOnClickListener {
           if (TextUtils.isEmpty(tagName)){
               showToastMessage("请选择房间标签")
               return@setOnClickListener
           }
            val intent = Intent()
            intent.putExtra("tabid",tabid)
            intent.putExtra("tagName",tagName)
            setResult(1854,intent)
            finish()
        }
    }
}