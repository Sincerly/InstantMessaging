package com.ysxsoft.imtalk.fragment

import android.content.Intent
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.youth.banner.listener.OnBannerListener
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.FamilyJz1Adapter
import com.ysxsoft.imtalk.adapter.FamilyJz2Adapter
import com.ysxsoft.imtalk.bean.BannerBean
import com.ysxsoft.imtalk.bean.FamilyListBean
import com.ysxsoft.imtalk.bean.MFamilyBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.BannerDetailActivity
import com.ysxsoft.imtalk.view.MyFamilyActivity
import com.ysxsoft.imtalk.view.MyFamilyGuideActivity
import com.ysxsoft.imtalk.widget.banner.GlideImageLoader
import kotlinx.android.synthetic.main.fm_family_jz.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.*

/**
 * 家族-家族
 */
class FamilyJzFragment : BaseFragment(), OnBannerListener {
    override fun OnBannerClick(position: Int) {
        val banner_id = datas!!.get(position).id.toString()
        val intent = Intent(mContext, BannerDetailActivity::class.java)
        intent.putExtra("banner_id", banner_id)
        startActivity(intent)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fm_family_jz
    }

    private var gcAdapter: FamilyJz1Adapter? = null
    private var jzAdapter: FamilyJz2Adapter? = null
    private var fm_id: String? = null
    private var is_fmy: String? = null
    var datas: MutableList<BannerBean.DataBean>? = null
    var urls = ArrayList<String>() as MutableList<String>
    override fun onResume() {
        super.onResume()
        initView()
        JzGCData()
        JzData()
        LunBoData()
    }

    private fun LunBoData() {
        NetWork.getService(ImpService::class.java)
                .HomeBanner("3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BannerBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: BannerBean?) {
                        if (t!!.code == 0) {
                            datas = t.data
                            if (urls.size > 0) {
                                urls.clear()
                            }
                            for (bean in t.data) {
                                urls.add(bean.pic)
                            }
                            banner.setImages(urls)
                                    .setImageLoader(GlideImageLoader())
                                    .setOnBannerListener(this@FamilyJzFragment)
                                    .start()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun JzData() {
        NetWork.getService(ImpService::class.java)
                .mFamily(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MFamilyBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: MFamilyBean?) {
                        if (t!!.code == 0) {
                            ll_jz.visibility = View.VISIBLE
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.fmy_pic, iv_jz_tx)
                            fm_id = t.data.id
                            is_fmy = t.data.is_fmy
                            tv_jz_name.setText(t.data.fmy_name)
                            tv_jz_id.setText("家族ID：" + t.data.fmy_sn)
                            tv_jz_cy.setText("家族成员：" + t.data.fmy_num)
                        } else {
                            ll_jz.visibility = View.GONE
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    //获取家族广场数据
    private fun JzGCData() {
        NetWork.getService(ImpService::class.java)
                .familyList(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FamilyListBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: FamilyListBean?) {
                        if (t!!.code == 0) {
                            recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                            gcAdapter = FamilyJz1Adapter(mContext)
                            recyclerView.adapter = gcAdapter
                            gcAdapter!!.addAll(t.data)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    private fun initView() {
//        recyclerView2.layoutManager = LinearLayoutManager(mContext)
//        jzAdapter = FamilyJz2Adapter(mContext)
//        recyclerView2.adapter = jzAdapter
//
//        val lists = listOf(ContentBean("测试1"), ContentBean("测试2"), ContentBean("测试3"),
//                ContentBean("测试4"), ContentBean("测试4"), ContentBean("测试4"))
//
//        jzAdapter!!.addAll(lists)

        rl_jz.setOnClickListener {
            MyFamilyActivity.startMyFamilyActivity(mContext, fm_id!!, is_fmy!!)
        }

        rl_jz_kf.setOnClickListener {
            showToastMessage("家族客服")
        }

        rl_jz_zn.setOnClickListener {
            val intent: Intent = Intent(mContext, MyFamilyGuideActivity::class.java)
            mContext.startActivity(intent)
        }

    }
}