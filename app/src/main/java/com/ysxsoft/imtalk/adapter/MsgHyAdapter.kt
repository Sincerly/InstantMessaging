package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.FansListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.GradeIconUtils
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.MyDataActivity
import com.ysxsoft.imtalk.widget.CircleImageView

class MsgHyAdapter(mContext: Context) : ListBaseAdapter<FansListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_msg_hy

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
        val charmIcon = GradeIconUtils.charmIcon(bean.ml_level.toInt())
        //魅力
        holder.getView<TextView>(R.id.tv_m)!!.setText(bean.ml_level)
        holder.getView<ImageView>(R.id.img_mei)!!.setImageResource(charmIcon[0])
        holder.getView<TextView>(R.id.tv_m)!!.setTextColor(charmIcon[1])
        val ints = GradeIconUtils.gradeIcon(bean.th_level.toInt())
        //等级
        holder.getView<TextView>(R.id.tv_dj)!!.setText(bean.th_level)
        holder.getView<ImageView>(R.id.img_zs)!!.setImageResource(ints[0])
        holder.getView<TextView>(R.id.tv_dj)!!.setTextColor(ints[1])

        //签名
        holder.getView<TextView>(R.id.tv_qm)!!.setText(bean.user_desc)
        holder.getView<CircleImageView>(R.id.iv_tx)!!.setOnClickListener {
            MyDataActivity.startMyDataActivity(mContext,bean.fs_id.toString(),"")
        }
    }
}