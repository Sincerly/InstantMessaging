package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.im.message.LobbyTextMessage
import com.ysxsoft.imtalk.utils.GradeIconUtils
import com.ysxsoft.imtalk.utils.displayUrlCyclo
import com.ysxsoft.imtalk.view.MyDataActivity
import io.rong.imkit.emoticon.AndroidEmoji
import io.rong.imkit.widget.AutoLinkTextView
import io.rong.imlib.model.Message

/**
 *Create By jyg
 *on 2019/9/24 0026
 */
class PalMessageAdapter(mContext: Context) : ListBaseAdapter<Message>(mContext){

    private var isInit : Boolean = false

    override val layoutId: Int
        get() = R.layout.item_palmessage_withavatar


    fun setLateData(list: Collection<Message>){
        setDataList(list)
        isInit = true
    }

    fun addData(msg : Message){
        mDataList.add(msg)
        notifyItemInserted(mDataList.size)
    }

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val item = mDataList[position]
        val tvContent = holder.getView<AutoLinkTextView>(R.id.tvContent)
        val tvMeili = holder.getView<TextView>(R.id.tvMeili)
        val tvZuan = holder.getView<TextView>(R.id.tvZuan)
        val tvNick = holder.getView<TextView>(R.id.tvNick)

        if (item.content is LobbyTextMessage){
            tvContent?.visibility = View.VISIBLE
            val msg = item.content as LobbyTextMessage
            val extra = Gson().fromJson(msg.extra, UserInfoBean.DataBean::class.java)
            val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
            ivAvatar?.displayUrlCyclo(extra.icon)
            ivAvatar?.setOnClickListener {
                MyDataActivity.startMyDataActivity(mContext, extra.uid, if (extra.uid == AuthManager.getInstance().currentUserId) "myself" else "")
            }

            val spannable = SpannableStringBuilder(msg.content)
            val spannable1 = AndroidEmoji.replaceEmojiWithText(spannable)
            AndroidEmoji.ensure(spannable1)
            tvContent?.text = spannable1
            val array = GradeIconUtils.charmIcon(extra.charm_level)
            tvMeili?.setCompoundDrawablesRelativeWithIntrinsicBounds( array[0], 0, 0, 0)
            tvMeili?.setTextColor(ContextCompat.getColor(mContext, array[1]))
            tvMeili?.text = extra.charm_level.toString()

            val grade = GradeIconUtils.gradeIcon(extra.user_level)
            tvZuan?.setCompoundDrawablesRelativeWithIntrinsicBounds( grade[0], 0, 0, 0)
            tvZuan?.setTextColor(ContextCompat.getColor(mContext, grade[1]))
            tvZuan?.text = extra.user_level.toString()
            tvNick?.text = extra.nickname
            tvNick?.setCompoundDrawablesRelativeWithIntrinsicBounds(if (extra.sex == "1") R.mipmap.icon_nan else R.mipmap.icon_nv, 0, 0, 0)
        }
    }

}