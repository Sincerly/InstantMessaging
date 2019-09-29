package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.RoomLevelsAdapter
import com.ysxsoft.imtalk.adapter.SupperStarAdapter
import com.ysxsoft.imtalk.adapter.TyrantListAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.SupperStarBean
import com.ysxsoft.imtalk.bean.TyrantListBean
import com.ysxsoft.imtalk.com.RoomStarBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.DressMallActivity
import com.ysxsoft.imtalk.view.MyDataActivity
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.dialog_room_level.*
import kotlinx.android.synthetic.main.dialog_room_level.recyclerView
import kotlinx.android.synthetic.main.include_crown_bronze.*
import kotlinx.android.synthetic.main.include_crown_gold.*
import kotlinx.android.synthetic.main.include_crown_silver.*
import kotlinx.android.synthetic.main.include_dialog_room_level.*
import kotlinx.android.synthetic.main.room_level_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers



class RoomLevelDialog( context : Context, roomId : String) : BottomSheetDialog(context) {

    private var roomid = roomId
    private var flagTop : Int = 0
    private var flagBottom : Int = 0


    private lateinit var mAdapter1 : SupperStarAdapter
    private lateinit var mAdapter2 : TyrantListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_room_level)
        super.onCreate(savedInstanceState)
        window!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent)

        initView()
        getList()
    }

    private fun initView(){
        tabTop.addTab(tabTop.newTab().setText("魅力榜"))
        tabTop.addTab(tabTop.newTab().setText("土豪榜"))
        tabBottom.addTab(tabBottom.newTab().setText("日榜"))
        tabBottom.addTab(tabBottom.newTab().setText("周榜"))
        tabBottom.addTab(tabBottom.newTab().setText("总榜"))

        tabTop.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
            override fun onTabSelected(p0: TabLayout.Tab?) {
                flagTop = p0!!.position
                flagBottom = tabBottom.selectedTabPosition
                getList()
            }

        })
        tabBottom.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }
            override fun onTabSelected(p0: TabLayout.Tab?) {
                flagBottom = p0!!.position
                getList()
            }

        })

        initAdapter()
    }

    /**
     * 初始化adapter
     */
    private fun initAdapter(){
        mAdapter1 = SupperStarAdapter(context, 1)
        mAdapter2 = TyrantListAdapter(context, 1)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter1.setOnSupperStarListener(object :SupperStarAdapter.OnSupperStarListener{
            override fun onClick(position: Int) {
                val bean = mAdapter1.dataList.get(position)
                val giveDialog = GiveDialog(context,bean.uid.toString(), roomid)
                var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                    override fun BmClick() {

                    }

                    override fun BtxmClick() {

                    }

                    override fun ScmClick() {

                    }

                    override fun clickGiveGift(uid: String, nickname: String) {
                        RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                    }

                    override fun clickPrivateChat() {
                        RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                    }

                    override fun clickGiveZb() {
                        DressMallActivity.startDressMallActivity(context, roomid, "", bean.nickname)
                    }

                    override fun clickFoucsOn() {
                        FocusOnData(SpUtils.getSp(context, "uid"), roomid, "1")
                    }

                    override fun setManager() {

                    }

                    override fun removeManager() {

                    }

                    override fun setExit() {

                    }

                    override fun blackList() {

                    }
                })
                giveDialog.show()

            }
        })
        mAdapter2.setOnTyrantListener(object :TyrantListAdapter.OnTyrantListener{
            override fun onClick(position: Int) {
                val bean = mAdapter2.dataList.get(position)
                val giveDialog = GiveDialog(context,bean.uid.toString(), roomid)
                var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                    override fun BmClick() {

                    }

                    override fun BtxmClick() {

                    }

                    override fun ScmClick() {

                    }

                    override fun clickGiveGift(uid: String, nickname: String) {
                        RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                    }

                    override fun clickPrivateChat() {
                        RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                    }

                    override fun clickGiveZb() {
                        DressMallActivity.startDressMallActivity(context, roomid, "", bean.nickname)
                    }

                    override fun clickFoucsOn() {
                        FocusOnData(SpUtils.getSp(context, "uid"), roomid, "1")
                    }

                    override fun setManager() {

                    }

                    override fun removeManager() {

                    }

                    override fun setExit() {

                    }

                    override fun blackList() {

                    }
                })
                giveDialog.show()

            }
        })
    }

    /**
     * 请求网络
     */
    private fun getList(){
        when(flagTop){
            0->{//魅力榜
                roomSupperStarList()
            }
            1->{//土豪榜
                roomTyrantList()
            }
        }
    }

    /**
     * 土豪榜
     */
    private fun roomTyrantList() {
        NetWork.getService(ImpService::class.java)
                .roomTyrantList(++flagBottom, roomid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ t ->
                    if (t!!.code == 0) {
                        setTopThreeTyrant(t.data)
                        if (t.data.size > 3) {
                            mAdapter2.setDataList(t.data)
                            recyclerView.adapter = mAdapter2
                        }else{
                            mAdapter2.setDataList(ArrayList())
                        }
                    }else{//无数据
                        setTopThreeSupperStar(ArrayList())
                        mAdapter2.setDataList(ArrayList())
                    }
                }

    }
    /**
     * 魅力榜（巨星榜）
     */
    private fun roomSupperStarList() {
        NetWork.getService(ImpService::class.java)
                .roomSupperStarList(++flagBottom, roomid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ t ->
                    if (t!!.code == 0) {
                        setTopThreeSupperStar(t.data)
                        if (t.data.size > 3) {
                            mAdapter1.setDataList(t.data)
                            recyclerView.adapter = mAdapter1
                        }else{
                            mAdapter1.setDataList(ArrayList())
                        }
                    }else{//无数据
                        setTopThreeSupperStar(ArrayList())
                        mAdapter1.setDataList(ArrayList())
                    }
                }

    }

    /**
     * 清除前三
     */
    private fun clearTopThree(){
        ivTop1.displayResCyclo( R.mipmap.icon_zanwu)
        tvTop1Name.text = ""
        tvTop1Id.text = ""

        ivTop2.displayResCyclo(R.mipmap.icon_zanwu)
        tvTop2Name.text = ""
        tvTop2Id.text = ""
        tvTop2.text = ""

        ivTop3.displayResCyclo(R.mipmap.icon_zanwu)
        tvTop3Name.text = ""
        tvTop3Id.text = ""
        tvTop3.text = ""
    }

    /**
     * 设置前三名(土豪榜)
     */
    private fun setTopThreeTyrant(data: List<TyrantListBean.DataBean>) {
        clearTopThree()
        for (i in 0..2) {
            when (i) {
                0 ->
                    if (data.isNotEmpty()) {
                        ivTop1.displayUrlCyclo(data[i].icon)
                        tvTop1Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop1Id.text = ttid
                        tvTop1.text = data[i].award_gold
                    }
                1 ->
                    if (data.size >= 2) {
                        ivTop2.displayUrlCyclo(data[i].icon)
                        tvTop2Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop2Id.text = ttid
                        tvTop2.text = data[i].award_gold
                    }
                2 ->
                    if (data.size >= 3) {
                        ivTop3.displayUrlCyclo(data[i].icon)
                        tvTop3Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop3Id.text = ttid
                        tvTop3.text = data[i].award_gold
                    }
            }
        }
    }

    /**
     * 设置前三名(巨星榜)
     */
    private fun setTopThreeSupperStar(data: List<SupperStarBean.DataBean>) {
        clearTopThree()
        for (i in 0..2) {
            when (i) {
                0 ->
                    if (data.isNotEmpty()) {
                        ivTop1.displayUrlCyclo(data[i].icon)
                        tvTop1Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop1Id.text = ttid
                        tvTop1.text = data[i].award_gold
                        ivTop1.setOnClickListener {
                            val giveDialog = GiveDialog(context,data[i].uid.toString(), roomid)
                            var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                            giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                            giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                            giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                                override fun BmClick() {

                                }

                                override fun BtxmClick() {

                                }

                                override fun ScmClick() {

                                }

                                override fun clickGiveGift(uid: String, nickname: String) {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickPrivateChat() {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickGiveZb() {
                                    DressMallActivity.startDressMallActivity(context, roomid, "", data[i].nickname)
                                }

                                override fun clickFoucsOn() {
                                    FocusOnData(SpUtils.getSp(context, "uid"), roomid, "1")
                                }

                                override fun setManager() {

                                }

                                override fun removeManager() {

                                }

                                override fun setExit() {

                                }

                                override fun blackList() {

                                }
                            })
                            giveDialog.show()
                        }
                    }
                1 ->
                    if (data.size >= 2) {
                        ivTop2.displayUrlCyclo(data[i].icon)
                        tvTop2Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop2Id.text = ttid
                        tvTop2.text = data[i].award_gold
                        ivTop2.setOnClickListener {
                            val giveDialog = GiveDialog(context,data[i].uid.toString(), roomid)
                            var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                            giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                            giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                            giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                                override fun BmClick() {

                                }

                                override fun BtxmClick() {

                                }

                                override fun ScmClick() {

                                }

                                override fun clickGiveGift(uid: String, nickname: String) {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickPrivateChat() {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickGiveZb() {
                                    DressMallActivity.startDressMallActivity(context, roomid, "", data[i].nickname)
                                }

                                override fun clickFoucsOn() {
                                    FocusOnData(SpUtils.getSp(context, "uid"), roomid, "1")
                                }

                                override fun setManager() {

                                }

                                override fun removeManager() {

                                }

                                override fun setExit() {

                                }

                                override fun blackList() {

                                }
                            })
                            giveDialog.show()
                        }
                    }
                2 ->
                    if (data.size >= 3) {
                        ivTop3.displayUrlCyclo(data[i].icon)
                        tvTop3Name.text = data[i].nickname
                        val ttid = "ID:"+ data[i].tt_id
                        tvTop3Id.text = ttid
                        tvTop3.text = data[i].award_gold
                        ivTop3.setOnClickListener {
                            val giveDialog = GiveDialog(context,data[i].uid.toString(), roomid)
                            var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                            giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                            giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                            giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                                override fun BmClick() {

                                }

                                override fun BtxmClick() {

                                }

                                override fun ScmClick() {

                                }

                                override fun clickGiveGift(uid: String, nickname: String) {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickPrivateChat() {
                                    RongIM.getInstance().startPrivateChat(context, roomid, findNikeName.text.toString());
                                }

                                override fun clickGiveZb() {
                                    DressMallActivity.startDressMallActivity(context, roomid, "", data[i].nickname)
                                }

                                override fun clickFoucsOn() {
                                    FocusOnData(SpUtils.getSp(context, "uid"), roomid, "1")
                                }

                                override fun setManager() {

                                }

                                override fun removeManager() {

                                }

                                override fun setExit() {

                                }

                                override fun blackList() {

                                }
                            })
                            giveDialog.show()
                        }
                    }
            }
        }
    }
    /**
     * 关注
     */
    private fun FocusOnData(sp: String, userId: String, s: String) {
        val map = HashMap<String, String>()
        map.put("uid",userId)
        map.put("fs_id",sp)
        map.put("flag", "1")
//        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .fans(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        ToastUtils.showToast(context,t!!.msg)
                    }

                    override fun onCompleted() {
                    }
                })
    }
}