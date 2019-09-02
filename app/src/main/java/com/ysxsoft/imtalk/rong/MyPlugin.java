package com.ysxsoft.imtalk.rong;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.utils.MyApplication;
import com.ysxsoft.imtalk.utils.AppUtil;
import com.ysxsoft.imtalk.utils.FileUtils;
import com.ysxsoft.imtalk.utils.ToastUtils;

import java.io.File;
import java.util.List;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class MyPlugin implements IPluginModule,IPluginRequestPermissionResultCallback {
    Conversation.ConversationType conversationType;
    String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.img_s_tp);
    }

    @Override
    public String obtainTitle(Context context) {
        return "图片";
    }

    @Override
    public void onClick(Fragment currentFragment, RongExtension extension) {
        this.conversationType = extension.getConversationType();
        this.targetId = extension.getTargetId();
        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        if (PermissionCheckUtil.checkPermissions(currentFragment.getContext(), permissions)) {
            Intent intent = new Intent(currentFragment.getActivity(), PictureSelectorActivity.class);
            extension.startActivityForPluginResult(intent, 23, this);

        } else {
            extension.requestPermissionForPluginResult(permissions, 255, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            ImageMessage imageMessage = ImageMessage.obtain(null, uri);
//            RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, targetId, imageMessage, null, null, new RongIMClient.SendImageMessageCallback() {
//
//                @Override
//                public void onAttached(Message message) {
//                    //保存数据库成功
//                }
//
//                @Override
//                public void onError(Message message, RongIMClient.ErrorCode code) {
//                    //发送失败
//                }
//
//                @Override
//                public void onSuccess(Message message) {
//                    //发送成功
//                }
//
//                @Override
//                public void onProgress(Message message, int progress) {
//                    //发送进度
//                }
//            });
//        }
    }


    @Override
    public boolean onRequestPermissionResult(Fragment fragment, RongExtension extension, int i, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionCheckUtil.checkPermissions(fragment.getActivity(), permissions)) {
            Intent intent = new Intent(fragment.getActivity(), PictureSelectorActivity.class);
            extension.startActivityForPluginResult(intent, 23, this);
        } else {
            extension.showRequestPermissionFailedAlter(PermissionCheckUtil.getNotGrantedPermissionMsg(fragment.getActivity(), permissions, grantResults));
        }
        return true;
    }
}
