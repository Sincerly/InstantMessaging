package com.ysxsoft.imtalk.utils;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.permissions.RxPermissions;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.DressMallBean;
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils;
import com.ysxsoft.imtalk.impservice.ImpService;
import com.ysxsoft.imtalk.utils.CarUtils.OnDownLoadComplteListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CarUtils {

    /**
     *
     * @param activity
     */
    public static void playCar(Activity activity,String carUrl){
        FrameLayout f = activity.findViewById(android.R.id.content);
        if (carUrl.endsWith(".svga")) {
            //加载svga
            String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
            File file = new File(SDPATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            int index = carUrl.lastIndexOf("/");
            String destFileName = carUrl.substring(index, carUrl.length());
            File downloadFile = new File(SDPATH + destFileName);
            if (!downloadFile.exists()) {
                //文件不存在进行下载
                downloadGift(activity,carUrl,new OnDownLoadComplteListener(){

                    @Override
                    public void complete(String path) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //直接显示
                                SVGAImageView svgaImageView = new SVGAImageView(activity);
                                SVGAParser parser =new  SVGAParser(activity);
                                f.addView(svgaImageView);
                                FileInputStream inputStream = null;
                                try {
                                    inputStream = new FileInputStream(downloadFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                parser.decodeFromInputStream(inputStream, destFileName, new SVGAParser.ParseCompletion() {
                                    @Override
                                    public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                        int s = svgaVideoEntity.getFrames() / svgaVideoEntity.getFPS();
                                        svgaImageView.setVideoItem(svgaVideoEntity);
                                        svgaImageView.stepToFrame(0, true);
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                f.removeView(svgaImageView);
                                            }
                                        }, Long.valueOf(s*1000));
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                },true);
                            }
                        });
                    }
                });
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //直接显示
                        SVGAImageView svgaImageView = new SVGAImageView(activity);
                        SVGAParser parser =new  SVGAParser(activity);
                        f.addView(svgaImageView);
                        FileInputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(downloadFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        parser.decodeFromInputStream(inputStream, destFileName, new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                int s = svgaVideoEntity.getFrames() / svgaVideoEntity.getFPS();
                                svgaImageView.setVideoItem(svgaVideoEntity);
                                svgaImageView.stepToFrame(0, true);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        f.removeView(svgaImageView);
                                    }
                                }, Long.valueOf(s*1000));
                            }

                            @Override
                            public void onError() {
                            }
                        },true);
                    }
                });
            }
        } else {
            //静态图
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //直接显示
                    ImageView imageView = new ImageView(activity);
                    Glide.with(activity).load(carUrl).into(imageView);
                    f.addView(imageView);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f.removeView(imageView);
                        }
                    }, Long.valueOf(3*1000));
                }
            });
        }
    }

    public static void playCarPlayOne(Activity activity,String carUrl){
        FrameLayout f = activity.findViewById(android.R.id.content);
        SVGAImageView s=f.findViewWithTag("carSVGImageView");
        if(s!=null){
            f.removeView(s);
        }
        if (carUrl.endsWith(".svga")) {
            //加载svga
            String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
            File file = new File(SDPATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            int index = carUrl.lastIndexOf("/");
            String destFileName = carUrl.substring(index, carUrl.length());
            File downloadFile = new File(SDPATH + destFileName);
            if (!downloadFile.exists()) {
                //文件不存在进行下载
                downloadGift(activity,carUrl,new OnDownLoadComplteListener(){

                    @Override
                    public void complete(String path) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //直接显示
                                SVGAImageView svgaImageView = new SVGAImageView(activity);
                                svgaImageView.setTag("carSVGImageView");
                                SVGAParser parser =new  SVGAParser(activity);
                                f.addView(svgaImageView);
                                FileInputStream inputStream = null;
                                try {
                                    inputStream = new FileInputStream(downloadFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                parser.decodeFromInputStream(inputStream, destFileName, new SVGAParser.ParseCompletion() {
                                    @Override
                                    public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                        int s = svgaVideoEntity.getFrames() / svgaVideoEntity.getFPS();
                                        svgaImageView.setVideoItem(svgaVideoEntity);
                                        svgaImageView.stepToFrame(0, true);
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                f.removeView(svgaImageView);
                                            }
                                        }, Long.valueOf(s*1000));
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                },true);
                            }
                        });
                    }
                });
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //直接显示
                        SVGAImageView svgaImageView = new SVGAImageView(activity);
                        svgaImageView.setTag("carSVGImageView");
                        SVGAParser parser =new  SVGAParser(activity);
                        f.addView(svgaImageView);
                        FileInputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(downloadFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        parser.decodeFromInputStream(inputStream, destFileName, new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                int s = svgaVideoEntity.getFrames() / svgaVideoEntity.getFPS();
                                svgaImageView.setVideoItem(svgaVideoEntity);
                                svgaImageView.stepToFrame(0, true);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        f.removeView(svgaImageView);
                                    }
                                }, Long.valueOf(s*1000));
                            }

                            @Override
                            public void onError() {
                            }
                        },true);
                    }
                });
            }
        } else {
            //静态图
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //直接显示
                    ImageView imageView = new ImageView(activity);
                    Glide.with(activity).load(carUrl).into(imageView);
                    f.addView(imageView);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f.removeView(imageView);
                        }
                    }, Long.valueOf(3*1000));
                }
            });
        }
    }

    private static void downloadGift(Activity activity, String url, OnDownLoadComplteListener listener) {
        String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
        int index = url.lastIndexOf("/");
        String destFileName = url.substring(index, url.length());
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(SDPATH,destFileName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File file, int id) {
                        if (listener != null) {
                            listener.complete(file.getPath());
                        }
                    }
                });
    }

    interface OnDownLoadComplteListener {
        void complete(String path);
    }

    /**
     * 下载全部动画
     * @param activity
     */
    public static void downloadAll(Activity activity){
        RxPermissions rxPermissions=new RxPermissions(activity);
        if(rxPermissions.isGranted(READ_EXTERNAL_STORAGE)&&rxPermissions.isGranted(WRITE_EXTERNAL_STORAGE)){
            //拥有读写权限
            //下载座驾
            NetWork.INSTANCE.getService(ImpService.class)
                .DressMall("1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DressMallBean>(){

                        @Override
                        public void onNext(DressMallBean dressMallBean) {
                            if (dressMallBean.getCode()==0){
                                List<DressMallBean.DataBean> d=dressMallBean.getData();
                                if(d==null){
                                    return;
                                }
                                for (int i = 0; i <d.size() ; i++) {
                                    DressMallBean.DataBean item=d.get(i);
                                    String url=item.getGif_pic();

                                    int index = url.lastIndexOf("/");
                                    String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
                                    String destFileName = url.substring(index, url.length());
                                    File downloadFile = new File(SDPATH + destFileName);
                                    if (!downloadFile.exists()) {
                                        OkHttpUtils.get()
                                                .url(url)
                                                .build()
                                                .execute(new FileCallBack(SDPATH,destFileName) {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(File file, int id) {
                                                    }
                                                });
                                    }else{
                                        long size=item.getSize();
                                        if(size==downloadFile.length()){
                                            //数据完整
                                        }else{
                                            downloadFile.delete();
                                            OkHttpUtils.get()
                                                    .url(url)
                                                    .build()
                                                    .execute(new FileCallBack(SDPATH,destFileName) {
                                                        @Override
                                                        public void onError(Call call, Exception e, int id) {

                                                        }

                                                        @Override
                                                        public void onResponse(File file, int id) {
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

            //下载礼物
            NetWork.INSTANCE.getService(ImpService.class)
                    .DressMall("3")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DressMallBean>(){

                        @Override
                        public void onNext(DressMallBean dressMallBean) {
                            if (dressMallBean.getCode()==0){
                                List<DressMallBean.DataBean> d=dressMallBean.getData();
                                if(d==null){
                                    return;
                                }
                                for (int i = 0; i <d.size() ; i++) {
                                    DressMallBean.DataBean item=d.get(i);
                                    String url=item.getGif_pic();

                                    int index = url.lastIndexOf("/");
                                    String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
                                    String destFileName = url.substring(index, url.length());
                                    File downloadFile = new File(SDPATH + destFileName);
                                    if (!downloadFile.exists()) {
                                        OkHttpUtils.get()
                                                .url(url)
                                                .build()
                                                .execute(new FileCallBack(SDPATH,destFileName) {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(File file, int id) {
                                                    }
                                                });
                                    }else{
                                        long size=item.getSize();
                                        if(size==downloadFile.length()){
                                            //数据完整
                                        }else{
                                            downloadFile.delete();
                                            OkHttpUtils.get()
                                                    .url(url)
                                                    .build()
                                                    .execute(new FileCallBack(SDPATH,destFileName) {
                                                        @Override
                                                        public void onError(Call call, Exception e, int id) {

                                                        }

                                                        @Override
                                                        public void onResponse(File file, int id) {
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }
}
