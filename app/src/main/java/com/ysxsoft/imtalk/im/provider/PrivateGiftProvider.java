package com.ysxsoft.imtalk.im.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.im.message.PrivateGiftMessage;

import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imkit.widget.provider.TextMessageItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

@ProviderTag(messageContent = PrivateGiftMessage.class,showPortrait =true)
public class PrivateGiftProvider extends IContainerItemProvider.MessageProvider<PrivateGiftMessage>{

    class ViewHolder {
        TextView name;
        TextView toName;
        ImageView logo;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_private_gift, null);
        ViewHolder holder = new ViewHolder();
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
            holder.name.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.name.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.name.setText(content.getGiftName());
        //AndroidEmoji.ensure((Spannable) holder.name.getText());
    }

    @Override
    public Spannable getContentSummary(PrivateGiftMessage privateGiftMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, PrivateGiftMessage privateGiftMessage, UIMessage uiMessage) {

    }
}
