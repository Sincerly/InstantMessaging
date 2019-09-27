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
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.MyDressActivity
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.mydata_gift_fragment_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDataGiftFragment : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.mydata_gift_fragment_layout
    }

    private lateinit var myHearadapter: BaseQuickAdapter<SGiftBean.DataBean.ListInfoBean, BaseViewHolder>
    private lateinit var myGiftadapter: BaseQuickAdapter<SFGiftBean.DataBean.ListInfoBean, BaseViewHolder>

    var uid: String? = null
    var myself: String? = null
    var nikeName: String? = null
    var data: Int? = -1
    override fun onResume() {
        super.onResume()
        val bundle = this.arguments//得到从Activity传来的数据
        uid = bundle!!.getString("uid")
        myself = bundle!!.getString("myself")
        nikeName = bundle!!.getString("nikeName")
//        if ("myself".equals(myself)) {
//            ll_fs.visibility = View.GONE
//        } else {
//            ll_fs.visibility = View.VISIBLE
//        }
        fouceData()
        requestData()
        requestGiftData()
        initView()
    }

    private fun initView() {
        ll_add_fouce.setOnClickListener {
            //关注
            if (data==1){
                FocusOnData(AuthManager.getInstance().currentUserId,uid!!,"1")
            }else{
                FocusOnData(AuthManager.getInstance().currentUserId,uid!!,"2")
            }
        }
        tv_msg.setOnClickListener {
            //私信
            RongIM.getInstance().startPrivateChat(getActivity(), uid, nikeName);
        }

    }

    /**
     * 关注
     */
    private fun FocusOnData(sp: String, userId: String, s: String) {
        val map = HashMap<String, String>()
        map.put("uid", sp)
        map.put("fs_id", userId)
        map.put("flag", s)
//        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .fans(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            activity!!.onBackPressed()
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }


    private fun fouceData() {
        NetWork.getService(ImpService::class.java)
                .fans_status(SpUtils.getSp(mContext,"uid"),uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<FouceOnBean>{
                    override fun onError(e: Throwable?) {
                        Log.d("MyDataGiftFragment",e!!.message.toString())
                    }

                    override fun onNext(t: FouceOnBean?) {
                        if (t!!.code==0){
                             data = t.data
                            if (t.data==1){//未关注
                                img_fouce.setImageResource(R.mipmap.img_w_add)
                                tv_fouce.setText("关注")
                            }else{//已关注  取消
                                img_fouce.setImageResource(R.mipmap.img_w_dui)
                                tv_fouce.setText("已关注")
                            }
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    private fun requestGiftData() {
        NetWork.getService(ImpService::class.java)
                .my_gift1(uid!!, "3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SFGiftBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message.toString())
                    }

                    override fun onNext(t: SFGiftBean?) {
                        if (t!!.code == 0) {
                            tv_gift.setText("收到的礼物(" + t.data.sum + ")")
                            myGiftadapter = object : BaseQuickAdapter<SFGiftBean.DataBean.ListInfoBean, BaseViewHolder>(R.layout.my_gift_item_layout, t.data.listInfo) {
                                override fun convert(helper: BaseViewHolder?, item: SFGiftBean.DataBean.ListInfoBean?) {
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item!!.pic, helper!!.getView<ImageView>(R.id.img_tupian))
//                                    helper.getView<TextView>(R.id.tv_name)!!.setText(item.name)
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext,item.pic,helper.getView<ImageView>(R.id.img_tupian)!!)
                                    helper.getView<TextView>(R.id.tv_jb)!!.setText(item.award_num.toString())
                                }
                            }
                            recyclerView1.layoutManager = GridLayoutManager(mContext,3)
                            recyclerView1.adapter = myGiftadapter
                        }
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
                .subscribe(object : Observer<SGiftBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SGiftBean?) {
                        if (t!!.code == 0) {
                            tv_myhead.setText("头饰(" + t.data.sum + ")")
                            myHearadapter = object : BaseQuickAdapter<SGiftBean.DataBean.ListInfoBean, BaseViewHolder>(R.layout.my_data_gift_item_layout, t.data.listInfo) {
                                override fun convert(helper: BaseViewHolder?, item: SGiftBean.DataBean.ListInfoBean?) {
                                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item!!.pic, helper!!.getView<ImageView>(R.id.img_tupian))
                                    helper.getView<TextView>(R.id.tv_name)!!.setText(item.name)
                                    when (item.is_use) {
                                        0 -> {
                                            helper.getView<TextView>(R.id.tv_day)!!.setText("未使用")
                                        }
                                        1 -> {
                                            helper.getView<TextView>(R.id.tv_day)!!.setText("已使用")
                                        }
                                        2 -> {
                                            helper.getView<TextView>(R.id.tv_day)!!.setText("已过期")
                                        }
                                    }
                                    if ("myself".equals(myself)) {
                                        helper.itemView.setOnClickListener {
                                            startActivity(MyDressActivity::class.java)
                                        }
                                    }
                                }
                            }
                            recyclerView.layoutManager = GridLayoutManager(mContext,3)
                            recyclerView.adapter = myHearadapter
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}