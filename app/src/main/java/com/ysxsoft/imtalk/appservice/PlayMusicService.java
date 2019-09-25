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
import com.ysxsoft.imtalk.music.DBUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * create by Sincerly on 2019/1/17 0017
 **/
public class PlayMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private List<RoomMusicListBean.DataBean> musics = new ArrayList<>();
    private int position = 0;
    private boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    public class MyBind extends Binder{
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void musicPlay(String url) {
        isRunning = true;
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
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
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            isRunning = false;
            stop();
        }
    }
    public interface OnMusicPosition{
        void postion(int position);
    }

    public OnMusicPosition onMusicPosition;
    public void setOnMusicPosition(OnMusicPosition onMusicPosition){
        this.onMusicPosition = onMusicPosition;
    }

//    ///////////////////////////////////////////////////////////////////////////
//    // 下载音乐
//    ///////////////////////////////////////////////////////////////////////////
//    private void downloadMusic(int i,String url,String mid){
//        String webPath=url;
//        int index=webPath.lastIndexOf("/");
//        String destFileName=webPath.substring(index,webPath.length());
//
//        OkHttpUtils.get()
//                .url(url)
//                .build()
//                .execute(new FileCallBack(AppConfig.BASE_PATH, destFileName) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                    }
//
//                    @Override
//                    public void onResponse(File file, int id) {
//                        Log.e("tag",""+i);
//                        //缓存完毕
////                        MusicCache cache=new MusicCache();
////                        cache.mid=mid;
////                        cache.webPath=webPath;
////                        cache.uid=DBUtils.getUid();
////                        cache.nativePath=file.getAbsolutePath();
////                        cache.save();
//
//                        DBUtils.updateMusicCachePath(mid,file.getAbsolutePath());
////                        if(musics!=null){
////                            MusicCache m=musics.get(i);
////                            m.nativePath=file.getAbsolutePath();//更新url
////                        }
////                        if(DemoHelper.getInstance().isVoiceCalling||DemoHelper.getInstance().isVideoCalling){
////                            //正在语音通话/视频通话
////                            return;
////                        }else{
////                            //播放路径
////                            musicPlay(i,file.getAbsolutePath());
////                        }
//                    }
//                });
//    }
}
