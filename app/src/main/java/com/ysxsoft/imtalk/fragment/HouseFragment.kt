package com.ysxsoft.imtalk.fragment

import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgTableBean
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.ViewGroup
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgVpAdapter
import com.ysxsoft.imtalk.bean.HomeTableBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.view.SearchActivity
import kotlinx.android.synthetic.main.fragment_house.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class HouseFragment : BaseFragment() {

    var titles: MutableList<HomeTableBean.DataBean>? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_house
    }

    override fun initUi() {
        tv_title.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }
        topView.post {
            val params: ViewGroup.LayoutParams = topView.layoutParams
            params.height = getStateBar()
            topView.layoutParams = params
        }
        requestTable()
    }

    private fun requestTable() {
        NetWork.getService(ImpService::class.java)
                .homeTable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeTableBean>{
                    override fun onError(e: Throwable?) {
                        Log.d("HouseFragment",e!!.message.toString())
                    }

                    override fun onNext(t: HomeTableBean?) {
                        if (t!!.code == 0) {
                            titles = t.data
                            initAdapter()
                        }
                    }

                    override fun onCompleted() {

                    }
                })
    }

    private fun initAdapter() {
        val informfragments = ArrayList<FgTableBean<HouseItemFragment>>()
        for (i in 0 until titles!!.size) {
            tabLayoutHouse.addTab(titles!!.get(i).pname)
            informfragments.add(FgTableBean(HouseItemFragment.newInstance(i,titles!!.get(i).pids.toString()), titles!!.get(i).pids.toString(), i))
        }
        val fgVpAdapter = FgVpAdapter(childFragmentManager, informfragments)
        viewPagerHouse.adapter = fgVpAdapter
        viewPagerHouse.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutHouse.getTabLayout()))
        tabLayoutHouse.setupWithViewPager(viewPagerHouse)
    }
}