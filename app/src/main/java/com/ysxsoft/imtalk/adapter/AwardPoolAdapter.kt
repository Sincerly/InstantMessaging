package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/7/26 0026
 */
class AwardPoolAdapter(mContext: Context) : ListBaseAdapter<AwardListDataBean.DataBean.AwardListBean>(mContext){
    override val layoutId: Int
        get() = R.layout.wining_record_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_time)!!.visibility=View.GONE
        ImageLoadUtil.GlideHeadImageLoad(mContext,bean.sg_pic,holder.getView<CircleImageView>(R.id.img_head)!!)
        holder.getView<TextView>(R.id.tv_nikeName)!!.setText(bean.sg_name)
        holder.getView<TextView>(R.id.tv_money)!!.setText(bean.sg_gold+"金币")

    }
}