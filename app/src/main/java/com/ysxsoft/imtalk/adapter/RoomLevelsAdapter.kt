package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.RoomStarBean
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class RoomLevelsAdapter(mContext: Context):ListBaseAdapter<RoomStarBean.DataBean>(mContext){

    override val layoutId: Int
        get() = R.layout.room_dialog_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideHeadImageLoad(mContext,bean.icon,holder.getView<CircleImageView>(R.id.img_head)!!)
        holder.getView<TextView>(R.id.tv_NO)!!.setText(bean.key_id.toString())
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.nickname)
        holder.getView<TextView>(R.id.tv_id)!!.setText("ID："+bean.tt_id)
        holder.getView<TextView>(R.id.tv_level)!!.setText(bean.now_level)
        holder.getView<TextView>(R.id.tv_juli)!!.setText("距离前一名"+bean.next_user)
        holder.itemView.setOnClickListener {
            if (onClickRoomLevelsListener!=null){
                onClickRoomLevelsListener!!.onClick(position)
            }
        }
    }

    interface OnClickRoomLevelsListener{
        fun onClick(position: Int)
    }
    private var onClickRoomLevelsListener: OnClickRoomLevelsListener?=null
    fun setOnClickRoomLevelsListener(onClickRoomLevelsListener: OnClickRoomLevelsListener){
        this.onClickRoomLevelsListener=onClickRoomLevelsListener
    }
}