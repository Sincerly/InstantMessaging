package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.MyInvitationRecodeBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

class InviteAdapter(mContext:Context) :ListBaseAdapter<MyInvitationRecodeBean.DataBean.UserInfoBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_invite

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        ImageLoadUtil.GlideHeadImageLoad(mContext,bean.icon,holder.getView<CircleImageView>(R.id.iv_tx)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.username)
        holder.getView<TextView>(R.id.tv_jb)!!.setText(bean.user_gold)
    }
}