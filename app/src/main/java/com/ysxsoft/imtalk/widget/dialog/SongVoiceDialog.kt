package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.voice_dialog_layout.*

/**
 *Create By 胡
 *on 2019/8/9 0009
 */
class SongVoiceDialog : ABSDialog{

    constructor(mContext: Context):super(mContext){
        val window = window
        window.setGravity(Gravity.TOP)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    override fun initView() {
        img_close.setOnClickListener {
            dismiss()
        }

        img_stop.setOnClickListener {
            dismiss()
            if (songVoiceListener!=null){
                songVoiceListener!!.StopSong()
            }
        }

        img_up_song.setOnClickListener {
            dismiss()
            if (songVoiceListener!=null){
                songVoiceListener!!.NextSong()
            }
        }

        img_setting.setOnClickListener {
            dismiss()
             if (songVoiceListener!=null){
                 songVoiceListener!!.SettingSong()
             }
        }

    }
    open fun seekBarMax(voice:Int){
        seekBar.max=voice
    }

    override fun getLayoutResId(): Int {
       return R.layout.voice_dialog_layout
    }

    interface SongVoiceListener{
        fun StopSong()
        fun NextSong()
        fun SettingSong()
    }
    private var songVoiceListener: SongVoiceListener?=null
    fun setSongVoiceListener(songVoiceListener: SongVoiceListener){
        this.songVoiceListener=songVoiceListener
    }
}