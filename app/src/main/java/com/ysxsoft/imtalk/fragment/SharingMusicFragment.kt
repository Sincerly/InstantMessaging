package com.ysxsoft.imtalk.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager

import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.SharingMusicAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.MusicListBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.include_my_song_book_empty.*
import kotlinx.android.synthetic.main.include_onlyrecyclerview.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.io.File
import android.util.Log
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import okhttp3.Call


/**
 * 共享音乐
 */
class SharingMusicFragment : BaseFragment() {

    var adapter: SharingMusicAdapter? = null
    var datas: MutableList<MusicListBean.DataBean>? = null
    var roomId: String? = null
    override fun getLayoutResId(): Int {
        return R.layout.fragment_sharing_music
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        roomId = arguments!!.getString("roomId")
    }

    override fun initUi() {
//        includeEmpty.visibility = View.VISIBLE
//        layoutList.visibility = View.GONE
        requestData()
        setClickListener()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .musicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<MusicListBean> {
                    override fun call(t: MusicListBean?) {
                        if (t!!.code == 0) {
                            datas = t.data
                            adapter = SharingMusicAdapter(mContext)
                            recyclerView.layoutManager = LinearLayoutManager(mContext)
                            recyclerView.adapter = adapter
                            adapter!!.addAll(datas!!)
                            adapter!!.setOnItemClickListener(object : SharingMusicAdapter.OnItemClickListener {
                                override fun onClick(position: Int) {
//                                    adapter!!.setSelect(position)
                                    val id = adapter!!.dataList.get(position).id.toString()
                                    val music_name = adapter!!.dataList.get(position).music_name
                                    val author_name = adapter!!.dataList.get(position).author_name
                                    val music_url = adapter!!.dataList.get(position).music_url
                                    addData(id,author_name, music_name, music_url)
                                }
                            })
                        }
                    }
                })

    }

    private fun addData(id: String, author_name: String, music_name: String, music_url: String) {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("room_id", roomId!!)
        map.put("music_id", id)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .setRoomMusic(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            downloadMusic(id,author_name, music_name, music_url)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
    private fun downloadMusic(id:String,author_name: String?, music_name: String?, music_url: String?) {
        val SDPATH = Environment.getExternalStorageDirectory().absolutePath + "/" + AppUtil.getCurrentPageName(mContext) + "/music/"
        val file = File(SDPATH)
        if (!file.exists()) {
            file.mkdirs()
        }
        val index = music_url!!.lastIndexOf(".mp3")
        val destFileName =id+"-"+ author_name + "-" + music_name +music_url.substring(index)
        Log.e("tag=音乐==", destFileName)
        val f = File(destFileName)
        if (f.exists()){
            return
        }
        Log.e("tag=音乐已存在>>>>>", "" + f.toString())
        OkHttpUtils.get()
                .url(music_url)
                .build()
                .execute(object : FileCallBack(SDPATH, destFileName) {
                    override fun onError(call: Call, e: Exception, id: Int) {
                    }

                    override fun inProgress(progress: Float, total: Long, id: Int) {}

                    override fun onResponse(file: File, id: Int) {
                        Log.e("tag=音乐>>>>>>>>>>>>>", "" + file.toString())
                    }
                })
    }

    private fun setClickListener() {
        btnAddMusic.setOnClickListener {
            //            includeEmpty.visibility = View.GONE
//            layoutList.visibility = View.VISIBLE
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            requestData()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(roomId: String) = SharingMusicFragment().apply {
            arguments = Bundle().apply {
                putString("roomId", roomId)
            }
        }
    }
}
