package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.FansListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.MyDataActivity
import com.ysxsoft.imtalk.widget.CircleImageView
import io.rong.imkit.RongIM

class MsgFsAdapter(mContext: Context) : ListBaseAdapter<FansListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_msg_fs

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);

        ImageLoadUtil.GlideHeadImageLoad(mContext, bean.icon, holder.getView<CircleImageView>(R.id.iv_tx)!!)

        if (TextUtils.equals("1", bean.sex)) {
            holder.getView<ImageView>(R.id.img_sex)!!.setBackgroundResource(R.mipmap.img_boy)
        } else {
            holder.getView<ImageView>(R.id.img_sex)!!.setBackgroundResource(R.mipmap.img_girl)
        }

        //昵称
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.nickname)
        //魅力
        holder.getView<TextView>(R.id.tv_m)!!.setText(bean.ml_level)
        //等级
        holder.getView<TextView>(R.id.tv_dj)!!.setText(bean.th_level)
        //签名
        holder.getView<TextView>(R.id.tv_qm)!!.setText(bean.user_desc)
        //关注
        if ("2".equals(bean.status)) {
            holder.getView<TextView>(R.id.tv_gz)!!.isSelected = false
            holder.getView<TextView>(R.id.tv_gz)!!.setText("相互关注")
        } else {
            holder.getView<TextView>(R.id.tv_gz)!!.isSelected = true
            holder.getView<TextView>(R.id.tv_gz)!!.setText("+关注")
        }
        holder.getView<TextView>(R.id.tv_gz)!!.setOnClickListener {
            if (onMsgFsListener!=null){
                onMsgFsListener!!.onClick(position)
            }
        }
        holder.itemView.setOnClickListener {
            RongIM.getInstance().startPrivateChat(mContext, bean.fs_id, bean.nickname);
        }
        holder.getView<CircleImageView>(R.id.iv_tx)!!.setOnClickListener {
            MyDataActivity.startMyDataActivity(mContext,bean.fs_id.toString(),"")
        }
    }

    interface OnMsgFsListener {
        fun onClick(position: Int)
    }

    private var onMsgFsListener: OnMsgFsListener? = null
    fun setOnMsgFsListener(onMsgFsListener: OnMsgFsListener) {
        this.onMsgFsListener = onMsgFsListener
    }
}