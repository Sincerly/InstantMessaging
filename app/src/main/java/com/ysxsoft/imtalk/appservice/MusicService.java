package com.ysxsoft.imtalk.appservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Create By 胡
 * on 2019/9/6 0006
 */
public class MusicService extends Service {
    private MyBinder mBinder = new MyBinder();
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    class MyBinder extends Binder implements IMusicService{

        @Override
        public void onPlay() {
            try {
                mediaPlayer.start();
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!mediaPlayer.isPlaying()){

            }
        }

        @Override
        public void onPause() {
            mediaPlayer.stop();
        }

        @Override
        public void onNext() {

        }
    }

}
