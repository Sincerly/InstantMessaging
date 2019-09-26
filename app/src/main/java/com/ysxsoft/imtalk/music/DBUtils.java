package com.ysxsoft.imtalk.music;

import com.ysxsoft.imtalk.appservice.MusicCache;
import com.ysxsoft.imtalk.chatroom.task.AuthManager;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

public class DBUtils {

    /**
     * 获取缓存的mids
     * @return
     */
    public static String getMusicCacheMids() {
        StringBuffer sb=new StringBuffer();
        List<MusicCache> musicCaches = LitePal.where("uid=? order by id asc", AuthManager.getInstance().getCurrentUserId()).find(MusicCache.class);
        if (musicCaches != null) {
            int size = musicCaches.size();
            for (int i = 0; i < size; i++) {
                MusicCache f = musicCaches.get(i);
                if(i==size-1){
                    sb.append(f.getMid());
                }else{
                    sb.append(f.getMid()+",");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 检测文件是否损坏
     * @return
     */
    public static boolean checkMusicFile(){
        boolean result = true;
        List<MusicCache> musicCaches = LitePal.where("uid=? order by id asc", AuthManager.getInstance().getCurrentUserId()).find(MusicCache.class);
        if (musicCaches != null) {
            int size = musicCaches.size();
            for (int i = 0; i < size; i++) {
                MusicCache f = musicCaches.get(i);
                if(!isMusicCached(f.getMid())){
                    //文件被损坏/或者清空
                    result=false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 判断音乐是否缓存
     *
     * @return
     */
    public static boolean isMusicCached(String mid) {
        boolean result = false;
        MusicCache musicCaches = LitePal.where("mid=? order by id asc", mid).findLast(MusicCache.class);
        if (musicCaches != null) {
            String filePath=musicCaches.getNativePath();
            if(filePath!=null){
                File file=new File(filePath);
                if(file.exists()){
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * 修改音乐缓存路径
     *
     * @return
     */
    public static boolean updateMusicCachePath(String mid,String nativePath) {
        boolean result = false;
        MusicCache musicCaches = LitePal.where("mid=? order by id asc", mid).findLast(MusicCache.class);
        if (musicCaches != null) {
            musicCaches.setNativePath(nativePath);
            result=musicCaches.update(musicCaches.getId())>0;
        }
        return result;
    }
}
