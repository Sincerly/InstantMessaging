package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.UserBlackListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

class BlackListAdapter(mContext:Context) :ListBaseAdapter<UserBlackListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_black_list

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        ImageLoadUtil.GlideHeadImageLoad(mContext,bean.icon,holder.getView<CircleImageView>(R.id.iv_tx)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.nickname)
        //移除
        holder.getView<TextView>(R.id.tv_del)!!.setOnClickListener {
            if (onDelListener!=null){
                onDelListener!!.onDelClick(position)
            }
        }
    }

    interface OnDelListener{
      fun  onDelClick(position: Int)
    }
    private var onDelListener: OnDelListener?=null
    fun setOnDelListener(onDelListener: OnDelListener){
        this.onDelListener=onDelListener
    }
}