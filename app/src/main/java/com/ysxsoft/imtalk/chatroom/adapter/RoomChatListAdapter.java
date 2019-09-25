package com.ysxsoft.imtalk.chatroom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.im.message.EggChatMessage;
import com.ysxsoft.imtalk.chatroom.im.message.GiftChatMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomGiftMessage;
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage;
import com.ysxsoft.imtalk.chatroom.utils.ResourceUtils;
import com.ysxsoft.imtalk.widget.CircleImageView;

import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class RoomChatListAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 5;
    private static final int VIEW_TYPE_CHAT_MESSAGE = 0;
    private static final int VIEW_TYPE_USER_CHANGED_INFO = 1;
    public static final int VIEW_TYPE_GIFT = 2;//礼物 小屏消息
    public static final int VIEW_TYPE_EGG = 3;//砸金蛋  小屏消息

    private Context context;
    public List<Message> messageList;
    private OnRoomChatListAdapterListener onRoomChatListAdapterListener;
    private OnGiftEggItemClickListener onGiftEggItemClickListener;

    public RoomChatListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageList != null ? messageList.size() : 0;
    }

    @Override
    public Message getItem(int position) {
        return messageList != null ? messageList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messageList.get(position);
        int viewType = getItemViewType(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = createView(viewType, parent);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        updateView(viewType, message, viewHolder);
        return convertView;
    }

    private View createView(int viewType, ViewGroup parent) {
        View contentView = null;
        if (viewType == VIEW_TYPE_CHAT_MESSAGE) {
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.chatroom_item_chatlist_chat, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatarIv = contentView.findViewById(R.id.chatroom_item_chatlist_iv_avatar);
            viewHolder.nickNameTv = contentView.findViewById(R.id.chatroom_item_chatlist_tv_nickname);
            viewHolder.messageTv = contentView.findViewById(R.id.chatroom_item_chatlit_tv_message);
            viewHolder.zsvalue = contentView.findViewById(R.id.tv_zs_num);
            contentView.setTag(viewHolder);
        } else if (viewType == VIEW_TYPE_USER_CHANGED_INFO) {
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.chatroom_item_chatlist_info, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatarIv = contentView.findViewById(R.id.chatroom_item_chatlist_iv_avatar);
            viewHolder.nickNameTv = contentView.findViewById(R.id.chatroom_item_chatlist_tv_nickname);
            viewHolder.messageTv = contentView.findViewById(R.id.chatroom_item_chatlit_tv_message);
            contentView.setTag(viewHolder);
        } else if (viewType == VIEW_TYPE_GIFT) {
            //礼物消息
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.chatroom_item_gift, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.giftFromName = contentView.findViewById(R.id.fromName);
            viewHolder.giftToName = contentView.findViewById(R.id.toName);
            viewHolder.giftInfo = contentView.findViewById(R.id.giftInfo);
            viewHolder.giftPic = contentView.findViewById(R.id.giftPic);
            contentView.setTag(viewHolder);
        } else if (viewType == VIEW_TYPE_EGG) {
            //砸金蛋消息
            LayoutInflater inflater = LayoutInflater.from(context);
            contentView = inflater.inflate(R.layout.chatroom_item_egg, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.eggFromName = contentView.findViewById(R.id.fromName);
            viewHolder.giftName = contentView.findViewById(R.id.eggName);
            viewHolder.giftPrice = contentView.findViewById(R.id.eggPrice);
            contentView.setTag(viewHolder);
        } else {

        }
        return contentView;
    }

    private void updateView(int viewType, Message message, ViewHolder viewHolder) {
        // 文本消息
        if (viewType == VIEW_TYPE_CHAT_MESSAGE) {
            TextMessage textMessage = (TextMessage) message.getContent();
            if (textMessage.getUserInfo()!=null){
                viewHolder.nickNameTv.setText(textMessage.getUserInfo().getName() + ":");
            }
            viewHolder.messageTv.setText(textMessage.getContent());
            viewHolder.zsvalue.setText(textMessage.getExtra());
            // 房间人员变动消息
        } else if (viewType == VIEW_TYPE_USER_CHANGED_INFO) {
            RoomMemberChangedMessage memberMessage = (RoomMemberChangedMessage) message.getContent();
            if (memberMessage.getUserInfo() != null) {
                viewHolder.nickNameTv.setText(memberMessage.getUserInfo().getName() + ":");
            }
            RoomMemberChangedMessage.RoomMemberAction roomMemberAction = memberMessage.getRoomMemberAction();
            if (roomMemberAction == RoomMemberChangedMessage.RoomMemberAction.JOIN) {
                String carName="";
                if(memberMessage.getCarName()!=null&&!"".equals(memberMessage.getCarName())){
                    carName="乘着"+memberMessage.getCarName();
                }
                viewHolder.messageTv.setText((carName)+context.getString(R.string.chatroom_user_enter));
            }else if (roomMemberAction == RoomMemberChangedMessage.RoomMemberAction.LEAVE) {
                viewHolder.nickNameTv.setVisibility(View.GONE);
                viewHolder.messageTv.setVisibility(View.GONE);
                viewHolder.messageTv.setText(R.string.chatroom_user_quit);
            } else if (roomMemberAction == RoomMemberChangedMessage.RoomMemberAction.KICK) {
                viewHolder.messageTv.setText(R.string.chatroom_user_kick);
            }
        } else if (viewType == VIEW_TYPE_GIFT) {
            GiftChatMessage giftChatMessage = (GiftChatMessage) message.getContent();
            viewHolder.giftFromName.setText(giftChatMessage.getName());
            viewHolder.giftToName.setText(giftChatMessage.getToName());
//            viewHolder.giftInfo.setText(giftChatMessage.getGiftName() + "x" + giftChatMessage.getGiftNum());
            viewHolder.giftFromName.setOnClickListener(new OnUserClickListener(giftChatMessage.getFromUid()));
            viewHolder.giftToName.setOnClickListener(new OnUserClickListener(giftChatMessage.getToUid()));
            Glide.with(context).load(giftChatMessage.getGiftStaticPic()).into(viewHolder.giftPic);
            viewHolder.giftInfo.setText("x" + giftChatMessage.getGiftNum());
        } else if (viewType == VIEW_TYPE_EGG) {
            EggChatMessage eggChatMessage = (EggChatMessage) message.getContent();
            viewHolder.eggFromName.setText(eggChatMessage.getName());//砸金蛋人
            viewHolder.giftName.setText(eggChatMessage.getGiftName());//砸出的礼物
//            viewHolder.giftPrice.setText(eggChatMessage.getGiftPrice());//砸出的礼物价值
            viewHolder.giftPrice.setText("");//砸出的礼物价值
            viewHolder.eggFromName.setOnClickListener(new OnUserClickListener(eggChatMessage.getGiftSendUserId()));
        } else {
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message.getContent() instanceof TextMessage) {
            return VIEW_TYPE_CHAT_MESSAGE;
        } else if (message.getContent() instanceof RoomMemberChangedMessage) {
            return VIEW_TYPE_USER_CHANGED_INFO;
        } else if (message.getContent() instanceof GiftChatMessage) {
            return VIEW_TYPE_GIFT;
        } else if (message.getContent() instanceof EggChatMessage) {
            return VIEW_TYPE_EGG;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public void setMessages(List<Message> messages) {
        messageList = messages;
    }

    private class ViewHolder {
        ImageView avatarIv;
        TextView nickNameTv;
        TextView messageTv;
        TextView zsvalue;
        //礼物start
        TextView giftFromName;
        TextView giftToName;
        TextView giftInfo;
        CircleImageView giftPic;
        //礼物end
        //砸金蛋start
        TextView eggFromName;
        TextView giftName;
        TextView giftPrice;
        //砸金蛋end
    }

    interface OnRoomChatListAdapterListener {
        void onClick(int position);
    }

    public void setOnRoomChatListAdapterListener(OnRoomChatListAdapterListener onRoomChatListAdapterListener) {
        this.onRoomChatListAdapterListener = onRoomChatListAdapterListener;
    }

    public interface OnGiftEggItemClickListener {
        void onClickUser(String userId);
        //void onClickToUser(String userId);
    }

    public void setOnGiftEggItemClickListener(OnGiftEggItemClickListener onGiftEggItemClickListener) {
        this.onGiftEggItemClickListener = onGiftEggItemClickListener;
    }

    class OnUserClickListener implements View.OnClickListener {
        private String uid;

        public OnUserClickListener(String uid) {
            this.uid = uid;
        }

        @Override
        public void onClick(View v) {
            if(onGiftEggItemClickListener!=null){
                onGiftEggItemClickListener.onClickUser(uid);
            }
        }
    }

}
