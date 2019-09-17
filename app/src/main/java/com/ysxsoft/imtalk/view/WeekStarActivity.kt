package com.ysxsoft.imtalk.view

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.*
import com.ysxsoft.imtalk.bean.StarBean
import com.ysxsoft.imtalk.bean.SupperStarBean
import com.ysxsoft.imtalk.bean.WeekStarBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.displayUrlCyclo
import kotlinx.android.synthetic.main.activity_week_star.*
import kotlinx.android.synthetic.main.include_crown_bronze.*
import kotlinx.android.synthetic.main.include_crown_gold.*
import kotlinx.android.synthetic.main.include_crown_silver.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_weekstar_3.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 周星榜
 * create by jyg
 * on 2019/9/16
 */
class WeekStarActivity : BaseActivity() {


    private lateinit var mAdapter1: WeekAdapter1 //本周礼物列表
    private lateinit var mAdapter2: WeekAdapter2 //上周明星列表
    private lateinit var mAdapter3: WeekAdapter3 //本周明星头部礼物列表
    private lateinit var mAdapter4: WeekAdapter4 //本周明星列表
    private lateinit var mAdapter5: WeekAdapter5 //周星奖励列表
    private lateinit var mAdapter6: WeekAdapter6 //礼物预告列表


    override fun getLayout(): Int {
        return R.layout.activity_week_star
    }

    override fun initUi() {
        setLightStatusBar(true)
        tvTitle.text = "周星榜"
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }

        initAdapter()
        postData()//获取页面数据
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {

        mAdapter1 = WeekAdapter1(mContext)
        mAdapter2 = WeekAdapter2(mContext)
        mAdapter3 = WeekAdapter3(mContext)
        mAdapter4 = WeekAdapter4(mContext)
        mAdapter5 = WeekAdapter5(mContext)
        mAdapter6 = WeekAdapter6(mContext)

        recyclerViewGift.layoutManager = GridLayoutManager(mContext, 3)
        recyclerViewGift.adapter = mAdapter1
        recyclerViewLastWeek.layoutManager = GridLayoutManager(mContext, 3)
        recyclerViewLastWeek.adapter = mAdapter2
        recyclerViewGiftTop.layoutManager = GridLayoutManager(mContext, 3)
        recyclerViewGiftTop.adapter = mAdapter3
        recyclerViewStarList.layoutManager = LinearLayoutManager(mContext)
        recyclerViewStarList.adapter = mAdapter4
        recyclerViewWeekStar.layoutManager = LinearLayoutManager(mContext)
        recyclerViewWeekStar.adapter = mAdapter5
        recyclerViewNextGift.layoutManager = GridLayoutManager(mContext, 3)
        recyclerViewNextGift.adapter = mAdapter6

        mAdapter3.setListener(object : WeekAdapter3.ItemClickListener {
            override fun itemClick(item: WeekStarBean.DataBean.WeekBean) {
                //根据头部礼物刷新明星排行榜
                postWeekStarData(item.id)
            }
        })
    }

    /**
     * 获取页面数据
     */
    private fun postData() {
        NetWork.getService(ImpService::class.java)
                .weekStarList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    if (t!!.code == 0) {
                        mAdapter1.setDataList(t.data.week)
                        mAdapter2.setDataList(t.data.last_star)
                        mAdapter3.setDataList(t.data.week)
                        mAdapter5.setDataList(t.data.last_star1)
                        mAdapter6.setDataList(t.data.next_gift)
                        tvGuize.text = t.data.pxgz
                        when (t.data.week.isNotEmpty()) {
                            true -> postWeekStarData(t.data.week[0].id)
                        }

                    }
                }
    }

    /**
     * 获取本周明星数据
     */
    private fun postWeekStarData(gift_zl_id: Int) {
        NetWork.getService(ImpService::class.java)
                .weekStar(gift_zl_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    if (t!!.code == 0) {
                        setTopThree(t.data)
                        mAdapter4.setDataList(t.data)
                    }
                }
    }

    /**
     * 设置前三名
     */
    private fun setTopThree(data: List<StarBean.DataBean>) {
        when (data.size) {
            0 -> tvNo1Name.text = "暂无"
            1 -> {
                tvNo2Name.text = "暂无"
                tvNo3Name.text = "暂无"
            }
            2 -> tvNo3Name.text = "暂无"
        }
        for (i in 0..2) {
            when (i) {
                0 ->
                    if (data.isNotEmpty()) {
                        ivCrownGold.displayUrlCyclo(data[i].icon)
                        tvNo1Name.text = data[i].nickname
                        tvNo1ID.text = "ID:" + data[i].tt_id
                        tvNo1Zuan.text = data[i].now_level.toString()
                    }
                1 ->
                    if (data.size >= 2) {
                        ivCrownSilver.displayUrlCyclo(data[i].icon)
                        tvNo2Name.text = data[i].nickname
                        tvNo2ID.text = "ID:" + data[i].tt_id
                        tvNo2Zuan.text = data[i].now_level.toString()
                        tvNo2Content.text = "距前一名" + data[i].next_user
                    }
                2 ->
                    if (data.size >= 3) {
                        ivCrownBronze.displayUrlCyclo(data[i].icon)
                        tvNo3Name.text = data[i].nickname
                        tvNo3ID.text = "ID:" + data[i].tt_id
                        tvNo3Zuan.text = data[i].now_level.toString()
                        tvNo3Content.text = "距前一名" + data[i].next_user
                    }
            }
        }
    }

}
