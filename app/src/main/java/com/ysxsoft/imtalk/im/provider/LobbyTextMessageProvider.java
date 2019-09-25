package com.ysxsoft.imtalk.im.provider;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.bean.UserInfoBean;
import com.ysxsoft.imtalk.chatroom.utils.MyApplication;
import com.ysxsoft.imtalk.im.message.LobbyTextMessage;
import com.ysxsoft.imtalk.utils.GradeIconUtils;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AutoLinkTextView;
import io.rong.imkit.widget.LinkTextViewMovementMethod;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(
        messageContent = LobbyTextMessage.class,
        showSummaryWithName = false,
        showReadState = true
)
public class LobbyTextMessageProvider extends IContainerItemProvider.MessageProvider<LobbyTextMessage>{

    private static final String TAG = "LobbyTextMessageProvider";

    public LobbyTextMessageProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_palmessage, (ViewGroup)null);
        ViewHolder holder = new ViewHolder();
        holder.message = view.findViewById(R.id.tvContent);
        holder.tvMeili = view.findViewById(R.id.tvMeili);
        holder.tvZuan = view.findViewById(R.id.tvZuan);
        holder.tvNick = view.findViewById(R.id.tvNick);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(LobbyTextMessage data) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, LobbyTextMessage data) {
        if (data == null) {
            return null;
        } else {
            String content = data.getContent();
            if (content != null) {
                if (content.length() > 100) {
                    content = content.substring(0, 100);
                }

                return new SpannableString(AndroidEmoji.ensure(content));
            } else {
                return null;
            }
        }
    }

    @Override
    public void onItemClick(View view, int i, LobbyTextMessage lobbyTextMessage, UIMessage uiMessage) {

    }

    @Override
    public void bindView(View v, int i, LobbyTextMessage textMessage, UIMessage data) {
        LobbyTextMessageProvider.ViewHolder holder = (LobbyTextMessageProvider.ViewHolder)v.getTag();
        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(R.drawable.icon_mytextmsg_right);
            holder.tvMeili.setVisibility(View.GONE);
            holder.tvZuan.setVisibility(View.GONE);
            holder.tvNick.setVisibility(View.GONE);
            holder.message.setTextColor(ContextCompat.getColor(MyApplication.mcontext, R.color.white));
        } else {
            holder.message.setBackgroundResource(R.drawable.icon_mytextmsg_left);
            holder.tvMeili.setVisibility(View.VISIBLE);
            holder.tvZuan.setVisibility(View.VISIBLE);
            holder.tvNick.setVisibility(View.VISIBLE);
            holder.message.setTextColor(ContextCompat.getColor(MyApplication.mcontext, R.color.colorAccent));
            UserInfoBean.DataBean bean = new Gson().fromJson(textMessage.getExtra(), UserInfoBean.DataBean.class);
            holder.tvNick.setText(bean.getNickname());

            int[] array = GradeIconUtils.Companion.charmIcon(bean.getCharm_level());
//            holder.tvMeili.setCompoundDrawables(ContextCompat.getDrawable(MyApplication.mcontext, array[0]), null, null, null);
            holder.tvMeili.setCompoundDrawablesWithIntrinsicBounds(array[0], 0, 0, 0);
            holder.tvMeili.setTextColor(ContextCompat.getColor(MyApplication.mcontext, array[1]));
            holder.tvMeili.setText(String.valueOf(bean.getCharm_level()));

            int[] grade = GradeIconUtils.Companion.gradeIcon(bean.getCharm_level());
//            holder.tvZuan.setCompoundDrawables(ContextCompat.getDrawable(MyApplication.mcontext, grade[0]), null, null, null);
            holder.tvZuan.setCompoundDrawablesWithIntrinsicBounds(grade[0], 0, 0, 0);
            holder.tvZuan.setTextColor(ContextCompat.getColor(MyApplication.mcontext, grade[1]));
            holder.tvZuan.setText(String.valueOf(bean.getUser_level()));
        }


        final AutoLinkTextView textView = holder.message;
        if (textMessage.getContent() != null) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(textMessage.getContent());
            SpannableStringBuilder spannable1 = AndroidEmoji.replaceEmojiWithText(spannable);
            AndroidEmoji.ensure(spannable1);
            int len = textMessage.getContent().length();
            if (v.getHandler() != null && len > 500) {
                v.getHandler().postDelayed(() -> textView.setText(spannable1), 50L);
            } else {
                textView.setText(spannable1);
            }
        }

        holder.message.setMovementMethod(new LinkTextViewMovementMethod(link -> {
            RongIM.ConversationBehaviorListener listener = RongContext.getInstance().getConversationBehaviorListener();
            RongIM.ConversationClickListener clickListener = RongContext.getInstance().getConversationClickListener();
            boolean result = false;
            if (listener != null) {
                result = listener.onMessageLinkClick(v.getContext(), link);
            } else if (clickListener != null) {
                result = clickListener.onMessageLinkClick(v.getContext(), link, data.getMessage());
            }

            if (listener == null && clickListener == null || !result) {
                String str = link.toLowerCase();
                if (str.startsWith("http") || str.startsWith("https")) {
                    Intent intent = new Intent("io.rong.imkit.intent.action.webview");
                    intent.setPackage(v.getContext().getPackageName());
                    intent.putExtra("url", link);
                    v.getContext().startActivity(intent);
                    result = true;
                }
            }
            return result;
        }));
        textView.stripUnderlines();
    }


    private static class ViewHolder {
        AutoLinkTextView message;
        TextView tvNick;
        TextView tvZuan;
        TextView tvMeili;

        boolean longClick;

        private ViewHolder() {
        }
    }
}
