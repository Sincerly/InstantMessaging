package com.ysxsoft.imtalk.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.appservice.PlayMusicService
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.EventBusBean
import com.ysxsoft.imtalk.bean.RoomMusicListBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.music.AudioUtils
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.displayRes
import kotlinx.android.synthetic.main.fragment_my_song_book.*
import kotlinx.android.synthetic.main.include_my_song_book_empty.*
import kotlinx.android.synthetic.main.include_onlyrecyclerview.*
import org.greenrobot.eventbus.EventBus
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *  我的曲库
 */
class MySongBookFragment : BaseFragment() {

    private lateinit var adapter: BaseQuickAdapter<RoomMusicListBean.DataBean, BaseViewHolder>

    override fun getLayoutResId(): Int {
        return R.layout.fragment_my_song_book
    }

    var lists: ArrayList<RoomMusicListBean.DataBean>? = null
    var roomId: String? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        roomId = arguments!!.getString("roomId")
    }

    override fun initUi() {
        seekBarMusic.progress = AudioUtils.getCurrentAudioVolume(mContext)
        seekBarMusic.setMax(AudioUtils.getMaxAudioVolume(context));//最大音量
        setClickListener()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .RoomMusicList(roomId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<RoomMusicListBean> {
                    override fun call(t: RoomMusicListBean?) {
                        if (t!!.code == 0) {
                            lists = t.data as ArrayList<RoomMusicListBean.DataBean>?
                            if (lists == null || lists!!.size <= 0) {
                                includeEmpty.visibility = View.VISIBLE
                                layoutList.visibility = View.GONE
                            } else {
                                includeEmpty.visibility = View.GONE
                                layoutList.visibility = View.VISIBLE
                            }
                            tv_sum_song.setText("共" + lists!!.size + "首歌")
                            initAdapter()
                        }
                    }
                })

    }

    private fun initAdapter() {
        adapter = object : BaseQuickAdapter<RoomMusicListBean.DataBean, BaseViewHolder>(R.layout.item_music, lists) {
            override fun convert(helper: BaseViewHolder?, item: RoomMusicListBean.DataBean?) {
                helper!!.getView<ImageView>(R.id.ivEnd)!!.visibility = View.VISIBLE
                helper.getView<RadioButton>(R.id.rbEnd)!!.visibility = View.GONE
                helper.getView<ImageView>(R.id.ivEnd)?.displayRes(R.mipmap.icon_music_delete)
                helper.getView<ImageView>(R.id.ivEnd)!!.setOnClickListener {
                    DelMusic(roomId, item!!.id)
                }
                helper.getView<TextView>(R.id.tvSongName)!!.setText(item!!.music_name)
                helper.getView<TextView>(R.id.tvName)!!.setText(item.a_name)
                helper.itemView.setOnClickListener {
                    EventBus.getDefault().post(EventBusBean(lists,helper.adapterPosition))
                    activity!!.onBackPressed()
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
    }

    private fun DelMusic(roomId: String?, id: Int) {
        val map = HashMap<String, String>()
        map.put("room_id", roomId!!)
        map.put("music_id", id.toString())
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .RoomMusicDel(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            requestData()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun setClickListener() {
        btnAddMusic.setOnClickListener {
            if (onAddMusicClickListener != null) {
                onAddMusicClickListener!!.onClick(btnAddMusic)
            }
            val intent = Intent("CHANGEFRAGMENT")
            activity!!.sendBroadcast(intent)
        }

        ivSetting.setOnClickListener {
            if (layoutBottomPlay.visibility == View.VISIBLE) {
                layoutBottomPlay.visibility = View.GONE
                layoutBottomSettiing.visibility = View.VISIBLE
            } else {
                layoutBottomPlay.visibility = View.VISIBLE
                layoutBottomSettiing.visibility = View.GONE
            }
        }

        seekBarVoice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = "$progress%"
                tvSeekVoice.text = percentage
            }
        })

        seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = "$progress%"
                tvSeekMusic.text = percentage
                AudioUtils.setAudioVolume(mContext, progress)
            }
        })
    }

    interface OnAddMusicClickListener {
        fun onClick(view: View)
    }

    private var onAddMusicClickListener: OnAddMusicClickListener? = null

    fun setOnAddMusicClickListener(onAddMusicClickListener: OnAddMusicClickListener) {
        this.onAddMusicClickListener = onAddMusicClickListener
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            requestData()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(roomId: String) = MySongBookFragment().apply {
            arguments = Bundle().apply {
                putString("roomId", roomId)
            }
        }
    }
}


