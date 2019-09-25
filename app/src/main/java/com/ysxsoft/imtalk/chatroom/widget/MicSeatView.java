package com.ysxsoft.imtalk.chatroom.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.RoomMicListBean;
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean;
import com.ysxsoft.imtalk.chatroom.model.MicState;
import com.ysxsoft.imtalk.chatroom.task.AuthManager;
import com.ysxsoft.imtalk.chatroom.task.RoomManager;
import com.ysxsoft.imtalk.chatroom.utils.MyApplication;
import com.ysxsoft.imtalk.chatroom.utils.ResourceUtils;
import com.ysxsoft.imtalk.utils.ImageLoadUtil;

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
public class MicSeatView extends FrameLayout {
    private ImageView micSeatIv;
    private ImageView micMuteIv;
    private ImageView img_head_wear, img_sex;
    private TextView nameTv, tv_room_manager;
    private MicSeatRippleView micSeatRippleView;
    private MicPositionsBean micInfo;
    private OnImageClickListener mOnImageClickLitener;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MicSeatView(@NonNull Context context) {
        super(context);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MicSeatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        View contentView = inflate(this.getContext(), R.layout.chatroom_item_mic_seat, this);
        micSeatIv = contentView.findViewById(R.id.chatroom_item_iv_mic_seat);
        micMuteIv = contentView.findViewById(R.id.chatroom_item_iv_mic_mute);
        tv_room_manager = contentView.findViewById(R.id.tv_room_manager);
        nameTv = contentView.findViewById(R.id.chatroom_item_tv_name);
        img_head_wear = contentView.findViewById(R.id.img_head_wear);
        img_sex = contentView.findViewById(R.id.img_sex);
        micSeatRippleView = contentView.findViewById(R.id.chatroom_item_rp_mic_ripple);
        micSeatIv.setBackground(null);
        micSeatIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnImageClickLitener != null) {
                    mOnImageClickLitener.onImageClick(view, micInfo.getSort() - 1);
                }
            }
        });
    }

    /**
     * 设置礼物值 和是否显示
     * <p>
     * //     * @param value  礼物值
     *
     * @param isshow 是否显示
     */
    public void setHeartValue(String value, boolean isshow) {
        if (isshow) {
            if (!"0".equals(micInfo.getUid())) {
                tv_room_manager.setVisibility(View.VISIBLE);
            } else {
                tv_room_manager.setVisibility(View.GONE);
            }
        } else {
            tv_room_manager.setVisibility(View.GONE);
        }
        tv_room_manager.setText(value);
    }

    public void updateMicState(MicPositionsBean micInfo) {
        this.micInfo = micInfo;
        String state = micInfo.getIs_lock_wheat();
        String micUserId = String.valueOf(micInfo.getUid());
        nameTv.setText("");
        String currentUserId = AuthManager.getInstance().getCurrentUserId();

        // 麦位是否为空状态
        if ((TextUtils.equals("0", micUserId)) && (!"0".equals(state)) && "0".equals(micInfo.getIs_wheat())) {
            setMicSeatEmpty();
            // 麦位是否被锁定
        } else if ("0".equals(state)) {
            lockMicSeat();
            // 麦位有用户
        } else if (!TextUtils.isEmpty(micUserId) && !TextUtils.equals("0", micUserId)) {
            setMicSeatAvatar(micInfo.getIcon(), micInfo.getUser_ts());
            nameTv.setText(micInfo.getNickname());
            nameTv.setTextColor(getResources().getColor(R.color.white));
            if (!TextUtils.isEmpty(micInfo.getSex())) {
                if ("1".equals(micInfo.getSex())) {
                    img_sex.setImageResource(R.mipmap.img_boy);
                } else {
                    img_sex.setImageResource(R.mipmap.img_girl);
                }
            }

//            if (currentUserId.equals(micUserId)) {
//                nameTv.setText(getResources().getText(R.string.me));
//                nameTv.setTextColor(getResources().getColor(R.color.btn_color));
//            } else {
//                nameTv.setText(micInfo.getNickname());
//                nameTv.setTextColor(getResources().getColor(R.color.white));
//            }
        }

        // 麦位是否被禁麦
        if ("1".equals(micInfo.getIs_oc_wheat())) { //闭麦
            setMicMuteState(true);
        } else {
            setMicMuteState(false);
        }
    }

    /**
     * 设置麦位上的头像
     *
     * @param resourceId
     */
    private void setMicSeatAvatar(String resourceId, String url) {
        micSeatIv.setBackgroundResource(R.drawable.chatroom_bg_room_linker_avatar);
        Glide.with(MyApplication.mcontext).load(resourceId).apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(R.mipmap.img_normal_head))
                .into(micSeatIv);
        ImageLoadUtil.INSTANCE.GlideGoodsImageLoad(MyApplication.mcontext, url, img_head_wear);
    }

    /**
     * 设置麦位静音
     */
    private void setMicMuteState(boolean isMute) {
        if (isMute) {
            micMuteIv.setVisibility(VISIBLE);
        } else {
            micMuteIv.setVisibility(GONE);
        }
    }

    /**
     * 锁定麦位
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void lockMicSeat() {
        micSeatIv.setImageDrawable(getResources().getDrawable(R.drawable.chatroom_bg_mic_seat_lock));
        micSeatIv.setBackground(null);
        tv_room_manager.setVisibility(View.GONE);
        img_head_wear.setImageBitmap(null);
        img_sex.setImageBitmap(null);
    }

    /**
     * 设置麦位为空
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setMicSeatEmpty() {
        micSeatIv.setImageDrawable(getResources().getDrawable(R.drawable.img_seat));
        micSeatIv.setBackground(null);
        tv_room_manager.setVisibility(View.GONE);
        img_head_wear.setImageBitmap(null);
        img_sex.setImageBitmap(null);
    }

    /**
     * 初始化麦位位置
     *
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init(int position) {
        micInfo = new MicPositionsBean();
        micInfo.setIs_lock_wheat("1");
        micInfo.setIs_oc_wheat("0");//add
        micInfo.setIs_wheat("0");//add
        micInfo.setSort(position);
        setMicSeatEmpty();
    }

    /**
     * 获取视图当前所在麦位
     *
     * @return
     */
    public int getPosition() {
        if (micInfo != null) {
            return micInfo.getSort() - 1;
        }
        return -1;
    }

    /**
     * 获取麦位信息
     *
     * @return
     */
    public MicPositionsBean getMicInfo() {
        return micInfo;
    }

    /**
     * 开始波纹动画
     */
    public void startRipple() {
        micSeatRippleView.enableRipple(true);
    }

    /**
     * 关闭波纹动画
     */
    public void stopRipple() {
        micSeatRippleView.enableRipple(false);
    }

    public interface OnImageClickListener {
        void onImageClick(View view, int position);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickLitener = onImageClickListener;
    }

}
