package com.ysxsoft.imtalk.rong;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.utils.ToastUtils;
import com.ysxsoft.imtalk.im.message.PrivateGiftMessage;
import com.ysxsoft.imtalk.widget.dialog.GiftBagDialog;
import com.ysxsoft.imtalk.widget.dialog.SendGiftDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class GiftPlugin implements IPluginModule,IPluginRequestPermissionResultCallback{
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.img_s_gift);
    }

    @Override
    public String obtainTitle(Context context) {
        return "送礼物";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        SendGiftDialog sendGiftDialog=new SendGiftDialog(fragment.getActivity(),rongExtension.getTargetId());
        sendGiftDialog.setOnSendGiftListener(new SendGiftDialog.OnSendGiftListener() {
            @Override
            public void onSendSuccess(@NotNull String from, @Nullable String to, @NotNull String giftId, @NotNull String giftNum) {
                ToastUtils.showToast("from"+from+" to:"+to+" giftId:"+giftId+" giftNum:"+giftNum);
                PrivateGiftMessage message=new PrivateGiftMessage();
                message.setGiftNum(giftNum);
                message.setGiftName("测试");
                message.setGiftUrl("http://www.baidu.com/a.png");

                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,rongExtension.getTargetId(),message, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        Log.e("tag","onAttached");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Log.e("tag","onSuccess:"+message.toString());
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Log.e("tag","onError:");
                    }
                });
            }
        });
        sendGiftDialog.show();
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

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
