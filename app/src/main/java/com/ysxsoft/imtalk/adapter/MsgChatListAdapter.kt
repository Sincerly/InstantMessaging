package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.opengl.Visibility
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

import io.rong.imlib.model.Conversation
import io.rong.message.TextMessage
import org.litepal.LitePal
import org.litepal.extension.find
import q.rorbin.badgeview.QBadgeView

/**
 * Create By 胡
 * on 2019/8/11 0011
 */
class MsgChatListAdapter(mContext: Context) : ListBaseAdapter<Conversation>(mContext) {
    override val layoutId: Int
        get() = R.layout.msg_chat_list_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        val beans = LitePal.where("uid=?", bean.targetId).find<com.ysxsoft.imtalk.bean.UserInfo>()
        if (beans.size > 0) {
            ImageLoadUtil.GlideHeadImageLoad(mContext, beans.get(beans.size-1).icon.toString(), holder.getView<CircleImageView>(R.id.img_head)!!)
            holder.getView<TextView>(R.id.tv_nikeName)!!.setText(beans.get(beans.size-1).nikeName)
            var tv_zsl = holder.getView<TextView>(R.id.tv_zs_num)
            var iv_sex = holder.getView<ImageView>(R.id.img_sex)
            if (beans[0].isSys!!) {
                tv_zsl!!.visibility = View.GONE
                iv_sex!!.visibility = View.GONE
            } else {
                tv_zsl!!.visibility = View.VISIBLE
                iv_sex!!.visibility = View.VISIBLE
                tv_zsl.setText(beans.get(beans.size-1).zsl)
                when (beans.get(beans.size-1).sex) {
                    "1" -> {
                        iv_sex.setImageResource(R.mipmap.img_boy)
                    }
                    "2" -> {
                        iv_sex.setImageResource(R.mipmap.img_girl)
                    }
                }
            }
        }

        val name = bean.objectName
        when (name) {
            "RC:TxtMsg" -> {
                val latestMessage = bean.latestMessage as TextMessage
                holder.getView<TextView>(R.id.tv_last_msg)!!.setText(latestMessage.content)
            }
            "RC:VcMsg" -> {
                holder.getView<TextView>(R.id.tv_last_msg)!!.setText("[语音]")
            }

            "RC:ImgMsg" -> {
                holder.getView<TextView>(R.id.tv_last_msg)!!.setText("[图片]")
            }
        }
        if (bean.unreadMessageCount > 0) {
            QBadgeView(mContext).bindTarget(holder.getView<TextView>(R.id.tv_point)!!).hide(false)
            QBadgeView(mContext).bindTarget(holder.getView<TextView>(R.id.tv_point)!!)
//                    .setBadgeBackgroundColor(R.color.btn_color)
//                    .setBadgeTextColor(R.color.white)
                    .setBadgeGravity(Gravity.CENTER or Gravity.END)
                    .setBadgeNumber(bean.unreadMessageCount)
        } else {
            QBadgeView(mContext).bindTarget(holder.getView<TextView>(R.id.tv_point)!!).hide(true)
        }
        if (!TextUtils.isEmpty(bean.sentTime.toString())) {
            holder.getView<TextView>(R.id.tv_time)!!.setText(AppUtil.FormarTime(AppUtil.AppTime.H_M_S, bean.sentTime))
        } else {
            holder.getView<TextView>(R.id.tv_time)!!.setText(AppUtil.FormarTime(AppUtil.AppTime.H_M_S, bean.receivedTime))
        }

        when (bean.conversationType) {
            Conversation.ConversationType.GROUP -> {//群聊
                holder.getView<ImageView>(R.id.img_sex)!!.visibility = View.GONE
                holder.getView<TextView>(R.id.tv_zs_num)!!.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            if (onChatMsgListener != null) {
                onChatMsgListener!!.onClick(position)
                bean.unreadMessageCount = 0
                QBadgeView(mContext).bindTarget(holder.getView<TextView>(R.id.tv_point)!!).hide(true)
            }
        }
    }

    interface OnChatMsgListener {
        fun onClick(position: Int)
    }

    private var onChatMsgListener: OnChatMsgListener? = null
    fun setOnChatMsgListener(onChatMsgListener: OnChatMsgListener) {
        this.onChatMsgListener = onChatMsgListener
    }
}
