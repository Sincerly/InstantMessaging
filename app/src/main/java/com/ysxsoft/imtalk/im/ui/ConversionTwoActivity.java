package com.ysxsoft.imtalk.im.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.utils.BaseActivity;
import com.ysxsoft.imtalk.view.MyDataActivity;

import java.util.Locale;

import io.rong.imlib.model.Conversation;

public class ConversionTwoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView right;
    private TextView title;
    private String targetId;
    private String conversationTitle;
    private Conversation.ConversationType conversationType;

    @Override
    public int getLayout() {
        return R.layout.conversation;
    }

    @Override
    public void initUi() {
        super.initUi();
//        targetId = intent.data!!.getQueryParameter("targetId")
//        conversationType = Conversation.ConversationType.valueOf(intent.data!!.lastPathSegment.toUpperCase(Locale.US))
//        title1 = intent.data!!.getQueryParameter("title")
//        val fragmentManage = supportFragmentManager
//        val fragement = fragmentManage.findFragmentById(R.id.conversation) as ConversationFragment
//        val uri = Uri.parse("rong://" + applicationInfo.packageName).buildUpon()
//                .appendPath("conversation")
//                .appendPath(conversationType!!.getName().toLowerCase())
//                .appendQueryParameter("targetId", targetId).build()
//        fragement.uri = uri
        Intent intent = getIntent();
        targetId = intent.getData().getQueryParameter("targetId");
        conversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        conversationTitle = intent.getData().getQueryParameter("title");
        initTitleBar();
    }

    private void initTitleBar() {
        back = findViewById(R.id.img_back);
        title = findViewById(R.id.tv_title);
        right = findViewById(R.id.img_right);
        back.setOnClickListener(this);
        right.setOnClickListener(this);
        if (conversationType == Conversation.ConversationType.PRIVATE) {
            right.setImageResource(R.mipmap.icon_user_profile);
        } else if (conversationType == Conversation.ConversationType.GROUP) {
            right.setImageResource(R.mipmap.icon_group_profile);
        }
        title.setText(conversationTitle);//显示标题
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                toDetailActivity();
                break;
        }
    }

    /**
     * 右侧资料按钮点击事件
     */
    private void toDetailActivity() {
        if (conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            //RongIM.getInstance().startPublicServiceProfile(this, conversationType, targetId)
        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
            if (targetId != null) {
                Intent intent = new Intent(mContext, MyDataActivity.class);
                intent.putExtra("uid", targetId);
                intent.putExtra("myself", "");
                mContext.startActivity(intent);
            }
        } else if (conversationType == Conversation.ConversationType.GROUP) {
            //群聊
        } else if (conversationType == Conversation.ConversationType.DISCUSSION) {
        }
    }

}
