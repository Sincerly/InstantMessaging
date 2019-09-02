package com.ysxsoft.imtalk.chatroom.task;

import android.content.Context;


import com.ysxsoft.imtalk.chatroom.net.HttpClient;
import com.ysxsoft.imtalk.chatroom.net.SealMicRequest;
import com.ysxsoft.imtalk.chatroom.utils.MyApplication;
import com.ysxsoft.imtalk.utils.BaseApplication;
import com.ysxsoft.imtalk.utils.SpUtils;


/**
 * 用户相关业务处理
 */
public class AuthManager {
    private static AuthManager instance;
    private SealMicRequest mRequest;

    public static AuthManager getInstance() {
        if (instance == null) {
            synchronized (AuthManager.class) {
                if (instance == null) {
                    instance = new AuthManager();
                }
            }
        }
        return instance;
    }

    private AuthManager() {
        mRequest = HttpClient.getInstance().getRequest();
    }

    public String getCurrentUserId() {
        return com.ysxsoft.imtalk.chatroom.utils.SpUtils.getSp(MyApplication.mcontext,"uid");
    }
}
