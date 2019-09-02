package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.SearchBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/8/16 0016
 */
class SearchAdapter(mContext: Context) : ListBaseAdapter<SearchBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.search_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideHeadImageLoad(mContext, bean.icon, holder.getView<CircleImageView>(R.id.img_head)!!)
        holder.getView<TextView>(R.id.tv_nikeName)!!.setText(bean.nickname)
        holder.getView<TextView>(R.id.tv_id)!!.setText("ID:" + bean.tt_id)
        holder.getView<TextView>(R.id.tv_num)!!.setText(bean.th_level)
        when (bean.sex) {
            "1" -> {
                holder.getView<ImageView>(R.id.img_sex)!!.setBackgroundResource(R.mipmap.img_boy)
            }
            "2" -> {
                holder.getView<ImageView>(R.id.img_sex)!!.setBackgroundResource(R.mipmap.img_girl)
            }

        }
    }
}