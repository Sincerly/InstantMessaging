package com.ysxsoft.imtalk.fragment

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.mipmap.myself
import com.ysxsoft.imtalk.adapter.MyDataFamilyAdapter
import com.ysxsoft.imtalk.adapter.PhotosAdpater
import com.ysxsoft.imtalk.adapter.PicturesAdapter
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.MFamilyBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.MyFamilyActivity
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.my_data_fragment.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDataFragment : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.my_data_fragment
    }

    var uid: String? = null
    var myself: String? = null
    override fun onResume() {
        super.onResume()
//my_data_fragment_item_layout
        val bundle = this.arguments//得到从Activity传来的数据
        uid = bundle!!.getString("uid")
        myself = bundle!!.getString("myself")
        initView()
        requestData()
        JzData()
    }
    var Jz_id:String?=null
    private fun JzData() {
        NetWork.getService(ImpService::class.java)
                .mFamily(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<MFamilyBean> {
                    override fun call(t: MFamilyBean?) {
                        if (t!!.code == 0) {
                             Jz_id = t.data.id
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.fmy_pic, img_head)
                            tv_familly_name.setText(t.data.fmy_name)
                            tv_jz_num.setText("家族ID：" + t.data.fmy_sn)
                        }
                    }
                })
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            tv_js.setText(t.data.user_desc)
                            val pictures = t.data.picture
                            pictures.add(0, UserInfoBean.DataBean.PictureBean())
                            val adapter = PhotosAdpater(mContext)
                            val manager = LinearLayoutManager(mContext)
                            manager.orientation = LinearLayoutManager.HORIZONTAL
                            recyclerView.layoutManager = manager
                            recyclerView.adapter = adapter
                            adapter.addAll(pictures)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {
        cl.setOnClickListener {
            if (TextUtils.isEmpty(Jz_id)){
                showToastMessage("您暂未加入任何家族")
                return@setOnClickListener
            }
            MyFamilyActivity.startMyFamilyActivity(mContext,uid!!,"3")
        }

        val lists = listOf(ContentBean("测试1"), ContentBean("测试2"), ContentBean("测试3"), ContentBean("测试4"), ContentBean("测试4"),
                ContentBean("测试1"), ContentBean("测试2"), ContentBean("测试3"), ContentBean("测试4"), ContentBean("测试4"))
        val mAdapter = MyDataFamilyAdapter(mContext)
        recyclerView1.layoutManager = LinearLayoutManager(mContext)
        recyclerView1.adapter = mAdapter
        mAdapter.addAll(lists)
    }
}