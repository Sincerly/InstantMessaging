package com.ysxsoft.imtalk.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.listener.OnBannerListener
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.BannerDetailActivity
import com.ysxsoft.imtalk.view.ChatRoomActivity
import com.ysxsoft.imtalk.widget.banner.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_house_item.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

private const val ARG_POSITION = "position"

/**
 * 房间子页面 （推荐，女神，男神，娱乐，情感电台。。。）
 */
class HouseItemFragment : BaseFragment(), OnBannerListener, SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        initUi()
    }

    override fun OnBannerClick(position: Int) {
        val banner_id = datas!!.get(position).id.toString()
        val intent = Intent(mContext, BannerDetailActivity::class.java)
        intent.putExtra("banner_id", banner_id)
        startActivity(intent)
    }

    lateinit var adapterHouse: BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterRecommend: BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterHouse1: BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>
    lateinit var adapterRecommend1: BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>

    private var position = 0
    private var pids: String? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_house_item
    }

    var datas: MutableList<BannerBean.DataBean>? = null
    var urls = ArrayList<String>() as MutableList<String>
    var tuijainDatas: MutableList<HomeRoomBean.DataBean.RoomListBean>? = null
    var hotDatas: MutableList<HomeRoomBean.DataBean.RoomListBean>? = null
    var roomLists: MutableList<HomeFRoomBean.DataBean.RoomListBean>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        position = arguments?.getInt(ARG_POSITION) ?: position
        pids = arguments!!.getString("pids")
    }

    private fun LunBoData() {
        NetWork.getService(ImpService::class.java)
                .HomeBanner("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BannerBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage("HouseItemFragment=LunBoData" + e!!.message.toString())
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
                                    .setOnBannerListener(this@HouseItemFragment)
                                    .start()
                        }
                    }

                    override fun onCompleted() {
                    }

                })

    }

    private fun NOHotData(pids: String) {
        NetWork.getService(ImpService::class.java)
                .RoomList1(pids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeFRoomBean> {
                    override fun onError(e: Throwable?) {
                        Log.d(this@HouseItemFragment.javaClass.name, e!!.message.toString())
                    }

                    override fun onNext(t: HomeFRoomBean?) {
                        if (t!!.code == 0) {
                            refreshLayout.isRefreshing = false
                            roomLists = t.data.get(0).roomList
                            init1Adapter()
                        } else {
                            refreshLayout.isRefreshing = false
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun init1Adapter() {
        if (roomLists!!.size>3) {
            adapterRecommend1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, roomLists!!.subList(0, 3)) {
                override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                    helper.getView<TextView>(R.id.tv_Content).text = item.room_name+"==="+item.room_id
                    helper.itemView.setOnClickListener {
                        //                    ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                        joinChatRoom(item.room_id.toString())
                    }
                }
            }
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.adapter = adapterRecommend1
        }else{
            adapterRecommend1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, roomLists) {
                override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
                    ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                    helper.getView<TextView>(R.id.tv_Content).text = item.room_name+"==="+item.room_id
                    helper.itemView.setOnClickListener {
                        //                    ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                        joinChatRoom(item.room_id.toString())
                    }
                }
            }
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.adapter = adapterRecommend1
        }

        adapterHouse1 = object : BaseQuickAdapter<HomeFRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_room, roomLists) {
            override fun convert(helper: BaseViewHolder, item: HomeFRoomBean.DataBean.RoomListBean) {
//                helper.getView<ImageView>(R.id.ivAvatar).displayRes(R.mipmap.icon_def)
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                helper.getView<TextView>(R.id.tv_name).text = item.room_name+"==="+item.room_id
                helper.getView<TextView>(R.id.tv_Tag).text = "#"+item.label_name
                helper.getView<TextView>(R.id.tv_Online).text =item.memCount+"人在线"
                helper.itemView.setOnClickListener {
//                    ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                    joinChatRoom(item.room_id.toString())
                }
            }
        }
        recyclerViewRoom.layoutManager = LinearLayoutManager(mContext)
        recyclerViewRoom.adapter = adapterHouse1

    }

    private fun tuiJianData(pids: String) {
        NetWork.getService(ImpService::class.java)
                .RoomList(pids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeRoomBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", e!!.message.toString())
                    }

                    override fun onNext(t: HomeRoomBean?) {
                        if (t!!.code == 0) {
                            refreshLayout.isRefreshing = false
                            tuijainDatas = t.data.get(0).roomList
                            tv_gftj.setText(t.data.get(0).cname)
                            hotDatas = t.data.get(1).roomList
                            onRefresh()
                        } else {
                            refreshLayout.isRefreshing = false
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    override fun initUi() {
        banner.visibility = if (0 == position) View.VISIBLE else View.GONE
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeResources(R.color.btn_color)
        LunBoData()
        if (position == 0) {
            tv_gftj.visibility = View.VISIBLE
            tuiJianData(pids!!)
            initAdapter()
        } else {
            tv_gftj.visibility = View.GONE
            NOHotData(pids!!)
        }
    }

    private fun initAdapter() {
        adapterRecommend = object : BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_recommend, tuijainDatas) {
            override fun convert(helper: BaseViewHolder, item: HomeRoomBean.DataBean.RoomListBean) {
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                helper.getView<TextView>(R.id.tv_Content).text = item.room_name
                helper.itemView.setOnClickListener {
//                    ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                    joinChatRoom(item.room_id.toString())
                }
            }
        }
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.adapter = adapterRecommend

        adapterHouse = object : BaseQuickAdapter<HomeRoomBean.DataBean.RoomListBean, BaseViewHolder>(R.layout.item_house_room, hotDatas) {
            override fun convert(helper: BaseViewHolder, item: HomeRoomBean.DataBean.RoomListBean) {
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.icon, helper.getView<ImageView>(R.id.ivAvatar))
                helper.getView<TextView>(R.id.tv_name).text = item.room_name
                helper.getView<TextView>(R.id.tv_Tag).text = "#"+item.label_name
                helper.getView<TextView>(R.id.tv_Online).text = item.memCount+"人在线"
                helper.itemView.setOnClickListener {
//                    ChatRoomActivity.starChatRoomActivity(mContext, item.room_id.toString())
                    joinChatRoom(item.room_id.toString())
                }
            }
        }
        recyclerViewRoom.layoutManager = LinearLayoutManager(mContext)
        recyclerViewRoom.adapter = adapterHouse

    }

    override fun onDestroy() {
        super.onDestroy()
        if (banner != null) {
            banner.stopAutoPlay()
        }
    }

    fun joinChatRoom(roomId:String){
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                ChatRoomActivity.starChatRoomActivity(mContext, roomId)
            }

            override fun onFail(errorCode: Int) {
                showToastMessage("HouseItemFragment==加入房间=="+errorCode)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, pids: String) =
                HouseItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, param1)
                        putString("pids", pids)
                    }
                }
    }

}
