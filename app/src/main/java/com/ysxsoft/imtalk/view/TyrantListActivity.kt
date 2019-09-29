package com.ysxsoft.imtalk.view

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.TyrantListAdapter
import com.ysxsoft.imtalk.bean.SupperStarBean
import com.ysxsoft.imtalk.bean.TyrantListBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.activity_tyrant_list.*
import kotlinx.android.synthetic.main.include_crown_bronze.*
import kotlinx.android.synthetic.main.include_crown_gold.*
import kotlinx.android.synthetic.main.include_crown_silver.*
import kotlinx.android.synthetic.main.include_toolbar.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *  create by jyg
 *  on 2019/9/16
 *
 *  土豪榜
 */
class TyrantListActivity : BaseActivity() {


    private lateinit var mAdapter: TyrantListAdapter
    private lateinit var customDialog: CustomDialog

    override fun getLayout(): Int {
        return R.layout.activity_tyrant_list
    }


    override fun initUi() {
        initToolBar(viewTop)
        setLightStatusBar(true)
        tvTitle.text = "土豪榜"
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        customDialog = CustomDialog(mContext, "正在加载....")

        initTabLayout()
        initAdapter()
        postData(1)
    }

    /**
     * 初始化tablayout
     */
    private fun initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("日榜"))
        tabLayout.addTab(tabLayout.newTab().setText("周榜"))
        tabLayout.addTab(tabLayout.newTab().setText("总榜"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {//选中后再次选中tab的逻辑
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {//添加未选中Tab的逻辑
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {//添加选中Tab的逻辑
                //请求刷新数据
                p0?.position?.let { postData(it + 1) }
            }

        })
    }

    /**
     * 初始化Adapter
     */
    private fun initAdapter() {
        mAdapter = TyrantListAdapter(mContext, 0)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = mAdapter
    }

    /**
     * 获取数据
     */
    private fun postData(type: Int) {
        customDialog.show()
        NetWork.getService(ImpService::class.java)
                .tyrantList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    if (t!!.code == 0) {
                        setTopThree(t.data)
                        mAdapter.setDataList(t.data)
                    } else {
                        setTopThree(ArrayList())
                        mAdapter.clear()
                    }
                    customDialog.dismiss()
                }
    }

    /**
     * 清除前三
     */
    private fun clearTopThree() {
        ivCrownGold.displayResCyclo(R.mipmap.icon_zanwu)
        tvNo1Name.text = ""
        tvNo1ID.text = ""
        tvNo1Zuan.text = ""
        tvNo1Zuan.visibility = View.GONE
        tvNo1Content.visibility = View.GONE

        ivCrownSilver.displayResCyclo(R.mipmap.icon_zanwu)
        tvNo2Name.text = ""
        tvNo2ID.text = ""
        tvNo2Zuan.text = ""
        tvNo2Zuan.visibility = View.GONE
        tvNo2Content.visibility = View.GONE

        ivCrownBronze.displayResCyclo(R.mipmap.icon_zanwu)
        tvNo3Name.text = ""
        tvNo3ID.text = ""
        tvNo3Zuan.text = ""
        tvNo3Zuan.visibility = View.GONE
        tvNo3Content.visibility = View.GONE
    }

    /**
     * 设置前三名
     */
    private fun setTopThree(data: List<TyrantListBean.DataBean>) {
        clearTopThree()
        for (i in 0..2) {
            when (i) {
                0 ->
                    if (data.isNotEmpty()) {
                        ivCrownGold.displayUrlCyclo(data[i].icon)
                        tvNo1Name.text = data[i].nickname
                        val ttid = "ID:" + data[i].tt_id
                        tvNo1Zuan.visibility = View.VISIBLE
                        tvNo1Content.visibility = View.VISIBLE
                        tvNo1ID.text = ttid
                        tvNo1Zuan.text = data[i].now_level.toString()

                    }
                1 ->
                    if (data.size >= 2) {
                        ivCrownSilver.displayUrlCyclo(data[i].icon)
                        tvNo2Name.text = data[i].nickname
                        val ttid = "ID:" + data[i].tt_id
                        tvNo2ID.text = ttid
                        tvNo2Zuan.visibility = View.VISIBLE
                        tvNo2Content.visibility = View.VISIBLE
                        tvNo2Zuan.text = data[i].now_level.toString()
                        tvNo2Content.text = "距前一名" + data[i].next_user
                    }
                2 ->
                    if (data.size >= 3) {
                        ivCrownBronze.displayUrlCyclo(data[i].icon)
                        tvNo3Name.text = data[i].nickname
                        val ttid = "ID:" + data[i].tt_id
                        tvNo3ID.text = ttid
                        tvNo3Zuan.visibility = View.VISIBLE
                        tvNo3Content.visibility = View.VISIBLE
                        tvNo3Zuan.text = data[i].now_level.toString()
                        tvNo3Content.text = "距前一名" + data[i].next_user
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        customDialog.dismiss()
    }
}
