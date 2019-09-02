package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.FamilyListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.MyFamilyActivity
import com.ysxsoft.imtalk.widget.CircleImageView

class FamilyJz1Adapter(mContext:Context) :ListBaseAdapter<FamilyListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_family_jz1

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        holder.itemView.setOnClickListener {
            MyFamilyActivity.startMyFamilyActivity(mContext,bean.id.toString(),bean.is_fmy)
        }
        holder.getView<TextView>(R.id.tv)!!.setText(bean.fmy_name)
        ImageLoadUtil.GlideGoodsImageLoad(mContext,bean.fmy_pic,holder.getView<CircleImageView>(R.id.iv)!!)

    }
}