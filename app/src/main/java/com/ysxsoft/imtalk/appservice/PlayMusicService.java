package com.ysxsoft.imtalk.appservice;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.RoomMusicListBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by Sincerly on 2019/1/17 0017
 **/
public class PlayMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private List<RoomMusicListBean.DataBean> musics = new ArrayList<>();
    private int position = 0;
    private boolean isRunning = false;
    private String url;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String url = intent.getStringExtra("music_url");
        try {
            mediaPlayer.reset();
            if (!TextUtils.isEmpty(url)){
                mediaPlayer.setDataSource(this, Uri.parse(url));
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();//异步准备
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("tag", "onPrepared");
                    mediaPlayer.start();//开始播放
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放完成
                    Log.e("tag", "资源已释放!");
                    next();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MyBind();
    }

    public class MyBind extends Binder implements IMusicService{
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }

        @Override
        public void onPlay() {
            start();
        }

        @Override
        public void onPause() {
            pause();
        }

        @Override
        public void onNext() {
            next();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    public void musicPlay(String url) {
        this.url = url;
        isRunning = true;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(url));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();//异步准备
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("tag", "onPrepared");
                    mediaPlayer.start();//开始播放
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放完成
                    Log.e("tag", "资源已释放!");
                    next();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isRunning = false;
        }
    }

    /**
     * 判断是否运行
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    public void setData(List<RoomMusicListBean.DataBean> m, int position) {
        musics.clear();
        musics.addAll(m);
        if (musics != null && musics.size() > 0) {
            musicPlay(musics.get(position).getMusic_url());
        }
    }

    public void next() {
        isRunning = true;
        position++;
        if (musics.size() > position) {
            musicPlay(musics.get(position).getMusic_url());
        } else {
            position = 0;
            musicPlay(musics.get(position).getMusic_url());
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isRunning = false;
        }
    }

    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isRunning = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning){
            mediaPlayer = new MediaPlayer();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mediaPlayer != null) {
//            isRunning = false;
//            stop();
//        }
    }

}
