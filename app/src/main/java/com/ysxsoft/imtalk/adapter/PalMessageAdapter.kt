package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.im.message.LobbyTextMessage
import com.ysxsoft.imtalk.utils.displayUrlCyclo
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
//        val layoutSticker = holder.getView<FrameLayout>(R.id.layoutSticker)

        val tvMeili = holder.getView<TextView>(R.id.tvMeili)
        val tvZuan = holder.getView<TextView>(R.id.tvZuan)
        val tvNick = holder.getView<TextView>(R.id.tvNick)


        if (item.content is LobbyTextMessage){
            tvContent?.visibility = View.VISIBLE
//            layoutSticker?.visibility = View.GONE
            val msg = item.content as LobbyTextMessage
            val extra = Gson().fromJson(msg.extra, UserInfoBean.DataBean::class.java)
            holder.getView<ImageView>(R.id.ivAvatar)?.displayUrlCyclo(extra.icon)

            val spannable = SpannableStringBuilder(msg.content)
            val spannable1 = AndroidEmoji.replaceEmojiWithText(spannable)
            AndroidEmoji.ensure(spannable1)
            tvContent?.text = spannable1
            tvMeili?.text = extra.charm_level.toString()
            tvZuan?.text = extra.user_level.toString()
            tvNick?.text = extra.nickname
        }
    }

}