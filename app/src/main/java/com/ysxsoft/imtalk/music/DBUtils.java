package com.ysxsoft.imtalk.music;

public class DBUtils {

//    /**
//     * 获取缓存的mids
//     * @return
//     */
//    public static String getMusicCacheMids() {
//        StringBuffer sb=new StringBuffer();
//        List<MusicCache> musicCaches = LitePal.where("uid=? order by id asc", DBUtils.getUid()).find(MusicCache.class);
//        if (musicCaches != null) {
//            int size = musicCaches.size();
//            for (int i = 0; i < size; i++) {
//                MusicCache f = musicCaches.get(i);
//                if(i==size-1){
//                    sb.append(f.mid);
//                }else{
//                    sb.append(f.mid+",");
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 检测文件是否损坏
//     * @return
//     */
//    public static boolean checkMusicFile(){
//        boolean result = true;
//        List<MusicCache> musicCaches = LitePal.where("uid=? order by id asc", DBUtils.getUid()).find(MusicCache.class);
//        if (musicCaches != null) {
//            int size = musicCaches.size();
//            for (int i = 0; i < size; i++) {
//                MusicCache f = musicCaches.get(i);
//                if(!isMusicCached(f.mid)){
//                    //文件被损坏/或者清空
//                    result=false;
//                    break;
//                }
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 判断音乐是否缓存
//     *
//     * @return
//     */
//    public static boolean isMusicCached(String mid) {
//        boolean result = false;
//        MusicCache musicCaches = LitePal.where("mid=? order by id asc", mid).findLast(MusicCache.class);
//        if (musicCaches != null) {
//            String filePath=musicCaches.nativePath;
//            if(filePath!=null){
//                File file=new File(filePath);
//                if(file.exists()){
//                    return true;
//                }
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 修改音乐缓存路径
//     *
//     * @return
//     */
//    public static boolean updateMusicCachePath(String mid,String nativePath) {
//        boolean result = false;
//        MusicCache musicCaches = LitePal.where("mid=? order by id asc", mid).findLast(MusicCache.class);
//        if (musicCaches != null) {
//            musicCaches.nativePath=nativePath;
//            result=musicCaches.update(musicCaches.id)>0;
//        }
//        return result;
//    }
}
