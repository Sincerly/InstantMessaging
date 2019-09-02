package com.ysxsoft.imtalk.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.mipmap.myself
import com.ysxsoft.imtalk.bean.FouceOnBean
import com.ysxsoft.imtalk.bean.SGiftBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.mydata_car_fragment_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDataCarFragment : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.mydata_car_fragment_layout
    }

    var uid: String? = null
    var myself: String? = null
    private lateinit var myHearadapter: BaseQuickAdapter<SGiftBean.DataBean.ListInfoBean, BaseViewHolder>
    override fun onResume() {
        super.onResume()
        val bundle = this.arguments//得到从Activity传来的数据
        uid = bundle!!.getString("uid")
        myself = bundle!!.getString("myself")
        if ("myself".equals(myself)) {
            ll_fs.visibility = View.GONE
            tv_car.visibility = View.GONE
        } else {
            ll_fs.visibility = View.VISIBLE
            tv_car.visibility = View.VISIBLE
        }
        requestData()
        initView()
    }

    private fun initView() {
        tv_fouce.setOnClickListener {//关注
            fouceData()
        }
        tv_msg.setOnClickListener {//私信
            RongIM.getInstance().startGroupChat(getActivity(), uid, "标题");
        }
        tv_car.setOnClickListener {//送座驾

        }

    }
    private fun fouceData() {
        NetWork.getService(ImpService::class.java)
                .fans_status(SpUtils.getSp(mContext,"uid"),uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FouceOnBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("MyDataCarFragment",e!!.message.toString())
                    }

                    override fun onNext(t: FouceOnBean?) {
                        showToastMessage(t!!.msg)
                    }

                    override fun onCompleted() {

                    }
                })

    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .my_gift(uid!!, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<SGiftBean> {
                    override fun call(t: SGiftBean?) {
                        if (t!!.code == 0) {
                            tv_carNum.setText("座驾（" + t.data.sum + ")")
                            myHearadapter = object : BaseQuickAdapter<SGiftBean.DataBean.ListInfoBean, BaseViewHolder>(R.layout.dress_mall_item_layout, t.data.listInfo) {
                                override fun convert(helper: BaseViewHolder?, item: SGiftBean.DataBean.ListInfoBean?) {
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item!!.pic, helper!!.getView<ImageView>(R.id.img_tupian))
                                    helper.getView<TextView>(R.id.tv_name)!!.setText(item.name)
                                    helper.getView<TextView>(R.id.tv_money)!!.setText(item.gold + "金币")
                                    helper.getView<TextView>(R.id.tv_day)!!.setText("/" + item.days + "天")
                                }
                            }
                            recyclerView.layoutManager = GridLayoutManager(mContext,2)
                            recyclerView.adapter = myHearadapter

                        }
                    }
                })

    }

}