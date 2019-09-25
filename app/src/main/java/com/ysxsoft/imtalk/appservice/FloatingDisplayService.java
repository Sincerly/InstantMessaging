package com.ysxsoft.imtalk.appservice;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.CommonBean;
import com.ysxsoft.imtalk.bean.UserInfoBean;
import com.ysxsoft.imtalk.chatroom.im.IMClient;
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage;
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo;
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil;
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient;
import com.ysxsoft.imtalk.chatroom.task.AuthManager;
import com.ysxsoft.imtalk.chatroom.task.ResultCallback;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;
import com.ysxsoft.imtalk.chatroom.utils.MyApplication;
import com.ysxsoft.imtalk.chatroom.utils.SpUtils;
import com.ysxsoft.imtalk.impservice.ImpService;
import com.ysxsoft.imtalk.utils.ImageLoadUtil;
import com.ysxsoft.imtalk.utils.NetWork;
import com.ysxsoft.imtalk.view.ChatRoomActivity;
import com.ysxsoft.imtalk.widget.CircleImageView;

import java.util.HashMap;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FloatingDisplayService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View displayView;
    private int mWindowWidth;
    private int mWindowHeight;
    private CircleImageView imageView;
    @org.jetbrains.annotations.Nullable
    private UserInfoBean mydatabean;
    private Handler changeImageHandler;
    private FrameLayout viewById;
    private String icon;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent();
        icon = intent.getStringExtra("icon");
        requestMyData();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        mWindowWidth = windowManager.getDefaultDisplay().getWidth();
        mWindowHeight = windowManager.getDefaultDisplay().getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = mWindowWidth;
        layoutParams.y = mWindowHeight - 500;

        changeImageHandler = new Handler(this.getMainLooper(), changeImageCallback);
    }

    private void requestMyData() {
        NetWork.INSTANCE.getService(ImpService.class)
                .GetUserInfo(AuthManager.getInstance().getCurrentUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if (userInfoBean.getCode() == 0) {
                            mydatabean = userInfoBean;

                        }
                    }
                });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                displayView = layoutInflater.inflate(R.layout.floatwindow_layout, null);
                displayView.setOnTouchListener(new FloatingOnTouchListener());
                imageView = displayView.findViewById(R.id.img_head);
                viewById = displayView.findViewById(R.id.fl);
                windowManager.addView(displayView, layoutParams);
            }
        }else {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.floatwindow_layout, null);
            displayView.setOnTouchListener(new FloatingOnTouchListener());
            imageView = displayView.findViewById(R.id.img_head);
            viewById = displayView.findViewById(R.id.fl);
            windowManager.addView(displayView, layoutParams);
        }
        changeImageHandler.sendEmptyMessageDelayed(0, 2000);
    }


    private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                if (displayView != null) {
                    Glide.with(MyApplication.mcontext).load(icon).into(imageView);
                    RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotate.setInterpolator(lin);
                    rotate.setDuration(5000);//设置动画持续周期
                    rotate.setRepeatCount(-1);//设置重复次数
                    rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    rotate.setStartOffset(10);//执行前的等待时间
                    imageView.setAnimation(rotate);

                    viewById.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quiteRoom("1");
                        }
                    });

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            joinChatRoom(mydatabean.getData().getNow_roomId());
                        }
                    });

                }

                changeImageHandler.sendEmptyMessageDelayed(0, 2000);
            }
            return false;
        }
    };


    private void quiteRoom(String kick) {
        RoomMemberChangedMessage message =new  RoomMemberChangedMessage();
        message.setCmd(2);//离开房间
        message.setTargetUserId(AuthManager.getInstance().getCurrentUserId());
        message.setTargetPosition(-1);
        message.setUserInfo(new UserInfo(AuthManager.getInstance().getCurrentUserId(), mydatabean.getData().getNickname(), Uri.parse("")));
        io.rong.imlib.model.Message obtain = io.rong.imlib.model.Message.obtain(mydatabean.getData().getNow_roomId(), Conversation.ConversationType.CHATROOM, message);
        RongIMClient.getInstance().sendMessage(obtain, null, null,new IRongCallback.ISendMessageCallback(){
            @Override
            public void onAttached(io.rong.imlib.model.Message message) {

            }

            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                NetWork.INSTANCE.getService(ImpService.class)
                        .tCRoom(AuthManager.getInstance().getCurrentUserId(), kick, mydatabean.getData().getNow_roomId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommonBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                if (commonBean.getCode() == 0) {
                                    removeUser(AuthManager.getInstance().getCurrentUserId(), mydatabean.getData().getNow_roomId());
                                }
                            }
                        });


            }

            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void removeUser(String currentUserId, String now_roomId) {
        HashMap map = new HashMap<String, String>();
        map.put("room_id", now_roomId);
        map.put("uid", currentUserId);
        RequestBody body = RetrofitUtil.createJsonRequest(map);
        NetWork.INSTANCE.getService(ImpService.class)
                .remove_user(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (commonBean.getCode() == 0) {
                            dismissWindow();
                            IMClient.getInstance().quitChatRoom(mydatabean.getData().getNow_roomId(), null);
                            RtcClient.getInstance().quitRtcRoom(mydatabean.getData().getNow_roomId(), null);
                        }
                    }
                });


    }

    private void joinChatRoom(String now_roomId) {
        RoomManager.getInstance().joinRoom(AuthManager.getInstance().getCurrentUserId(), now_roomId, "", new ResultCallback<DetailRoomInfo>() {
            @Override
            public void onSuccess(DetailRoomInfo detailRoomInfo) {
                Intent intent =new  Intent(FloatingDisplayService.this, ChatRoomActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("room_id",now_roomId);
                intent.putExtra("nikeName",mydatabean.getData().getNickname());
                intent.putExtra("icon",mydatabean.getData().getIcon());
                intent.putExtra("identical", "identical");
                startActivity(intent);
                dismissWindow();
            }

            @Override
            public void onFail(int errorCode) {

            }
        });
    }

    public void dismissWindow() {
        if (windowManager != null && displayView != null) {
            windowManager.removeViewImmediate(displayView);
//            windowManager.removeView(displayView);
            displayView = null;
            stopService(new Intent(this,FloatingDisplayService.class));
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;

//                case MotionEvent.ACTION_UP:
//                    layoutParams.x = mWindowWidth;
//                    windowManager.updateViewLayout(view, layoutParams);
//                    break;
                default:
                    break;
            }
            return false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && displayView != null) {
            windowManager.removeView(displayView);
        }
    }
}
