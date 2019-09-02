package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.GroupListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class JzQzAdapter(mContext: Context) : ListBaseAdapter<GroupListBean.DataBean.GroupInfoBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.activity_my_family_item

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        ImageLoadUtil.GlideHeadImageLoad(mContext, bean.group_avatar, holder.getView<CircleImageView>(R.id.img_head)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.group_name)
        holder.getView<TextView>(R.id.tv_member_num)!!.setText("成员：" + bean.group_num)

        when (bean.type) {
            "1" -> {
                holder.getView<ImageView>(R.id.img_ed)!!.visibility = View.VISIBLE
                holder.getView<TextView>(R.id.tv_join)!!.visibility = View.GONE
            }
            "2" -> {
                holder.getView<ImageView>(R.id.img_ed)!!.visibility = View.GONE
                when (bean.gr_status) {
                    "1" -> {
                        holder.getView<TextView>(R.id.tv_join)!!.visibility = View.VISIBLE
                    }
                    else -> {
                        holder.getView<TextView>(R.id.tv_join)!!.visibility = View.GONE
                    }
                }
            }
            "3" -> {
                holder.getView<ImageView>(R.id.img_ed)!!.visibility = View.GONE
                holder.getView<TextView>(R.id.tv_join)!!.visibility = View.VISIBLE
            }
        }
        holder.getView<TextView>(R.id.tv_join)!!.setOnClickListener {
            if (onJoinListener!=null){
                onJoinListener!!.onClick(position)
            }

        }
    }

    interface OnJoinListener {
        fun onClick(position: Int)
    }

    private var onJoinListener: OnJoinListener? = null
    fun setOnJoinListener(onJoinListener: OnJoinListener) {
        this.onJoinListener = onJoinListener
    }

}