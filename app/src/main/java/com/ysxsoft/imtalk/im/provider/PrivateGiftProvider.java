package com.ysxsoft.imtalk.im.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.im.message.PrivateGiftMessage;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = PrivateGiftMessage.class)
public class PrivateGiftProvider extends IContainerItemProvider.MessageProvider<PrivateGiftMessage>{

    class ViewHolder {
        LinearLayout bg;
        TextView name;
        TextView toName;
        ImageView logo;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_private_gift, null);
        ViewHolder holder = new ViewHolder();
        holder.bg = (LinearLayout) view.findViewById(R.id.bg);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.toName = (TextView) view.findViewById(R.id.toName);
        holder.logo = (ImageView) view.findViewById(R.id.logo);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int i, PrivateGiftMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.bg.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.bg.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        Glide.with(v.getContext()).load(content.getGiftUrl()).into(holder.logo);
        holder.name.setText(content.getGiftName()+" x"+content.getGiftNum());
        holder.toName.setText("赠送给"+ content.getToName());
    }

    @Override
    public Spannable getContentSummary(PrivateGiftMessage privateGiftMessage) {
        return new SpannableString("这是一条自定义消息CustomizeMessage");
    }

    @Override
    public void onItemClick(View view, int i, PrivateGiftMessage privateGiftMessage, UIMessage uiMessage) {
    }
}
