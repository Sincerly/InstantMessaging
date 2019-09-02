package com.ysxsoft.imtalk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import java.lang.ref.WeakReference


/**
 *Create By 胡
 *on 2019/8/8 0008
 */
class VolumeChangeObserver {
    companion object {
        var VOLUME_CHANGED_ACTION: String? = "android.media.VOLUME_CHANGED_ACTION";
        var EXTRA_VOLUME_STREAM_TYPE: String? = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    }

    var mAudioManager: AudioManager? = null
    var mContext: Context? = null
    private var mVolumeChangeListener: VolumeChangeListener? = null

    constructor(mContext: Context) {
        this.mContext = mContext
        mAudioManager = mContext.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    //获取系统最小媒体音量
    fun getCurrentMusicVolume(): Int {
        return if (mAudioManager != null) mAudioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC) else -1;
    }

    //获取系统最大媒体音量
    fun getMaxMusicVolume(): Int {
        return if (mAudioManager != null) mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC) else 15
    }

    interface VolumeChangeListener {
        //系统媒体音量变化
        fun onVolumeChanged(volume: Int)
    }


    var mVolumeBroadcastReceiver: VolumeBroadcastReceiver? = null
    /**
     * 解注册音量广播监听器，需要与 registerReceiver 成对使用
      */
    fun registerReceiver() {
        mVolumeBroadcastReceiver = VolumeBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction(VOLUME_CHANGED_ACTION);
        mContext!!.registerReceiver(mVolumeBroadcastReceiver, filter);
    }

    fun getVolumeChangeListener(): VolumeChangeListener {
        return mVolumeChangeListener!!;
    }
    fun setVolumeChangeListener(volumeChangeListener: VolumeChangeListener) {
        this.mVolumeChangeListener = volumeChangeListener;
    }


    var mRegistered = false

    fun unregisterReceiver() {
        if (mRegistered) {
            try {
                MyApplication.mcontext.unregisterReceiver(mVolumeBroadcastReceiver);
                mVolumeChangeListener = null;
                mRegistered = false;
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    class VolumeBroadcastReceiver : BroadcastReceiver() {

        var mObserverWeakReference: WeakReference<VolumeChangeObserver>? = null
        fun VolumeBroadcastReceiver(volumeChangeObserver: VolumeChangeObserver) {
            mObserverWeakReference = WeakReference<VolumeChangeObserver>(volumeChangeObserver)
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            if (VOLUME_CHANGED_ACTION.equals(intent!!.getAction()) && (intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC)) {
                val observer = mObserverWeakReference!!.get()
                if (observer != null) {
                    val listener = observer.getVolumeChangeListener()
                    if (listener != null) {
                        val volume = VolumeChangeObserver(context!!).getCurrentMusicVolume()
                        if (volume >= 0) {
                            listener.onVolumeChanged(volume);
                        }
                    }
                }
            }
        }

    }
}