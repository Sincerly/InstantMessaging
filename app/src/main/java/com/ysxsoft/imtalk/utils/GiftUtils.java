package com.ysxsoft.imtalk.utils;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.ysxsoft.imtalk.view.NotifyManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class GiftUtils{
    private Activity context;
    private List<String> data = new ArrayList<>();
    private boolean isRunning = false;

    public GiftUtils(Activity context) {
        this.context = context;
    }

    /**
     * 开启滚动
     */
    public synchronized void start() {
        if (data.size() > 0) {
            if (!isRunning) {
                isRunning = true;
                playGift(context,data.get(0));
            }
        }
    }

    public void setData(List<String> items) {
        this.data.clear();
        this.data.addAll(items);
    }

    public void addData(String item) {
        this.data.add(item);
        start();
    }

    public void playGift(Activity activity, String url){
        FrameLayout f = activity.findViewById(android.R.id.content);
        if (url.endsWith(".svga")) {
            //加载svga
            String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChatRoomActivity/svags/";
            File file = new File(SDPATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            int index = url.lastIndexOf("/");
            String destFileName = url.substring(index, url.length());
            File downloadFile = new File(SDPATH + destFileName);
            if (!downloadFile.exists()) {
                //文件不存在进行下载
                downloadGift(activity,url,new CarUtils.OnDownLoadComplteListener(){
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
                                                if (data.size() > 0) {
                                                    f.removeView(svgaImageView);
                                                    data.remove(0);
                                                    if (data.size() > 0) {
                                                        isRunning = false;
                                                        start();
                                                    } else {
                                                        isRunning = false;
                                                    }
                                                }
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
                                        if (data.size() > 0) {
                                            f.removeView(svgaImageView);
                                            data.remove(0);
                                            if (data.size() > 0) {
                                                isRunning = false;
                                                start();
                                            } else {
                                                isRunning = false;
                                            }
                                        }
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
                    Glide.with(activity).load(url).into(imageView);
                    f.addView(imageView);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f.removeView(imageView);
                            isRunning=false;
                        }
                    }, Long.valueOf(3*1000));
                }
            });
        }
    }

    private static void downloadGift(Activity activity, String url, CarUtils.OnDownLoadComplteListener listener) {
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
}
