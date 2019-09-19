package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.StarBean
import com.ysxsoft.imtalk.bean.UserBankListBean
import com.ysxsoft.imtalk.bean.WeekStarBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.displayResCyclo
import com.ysxsoft.imtalk.utils.displayUrl
import com.ysxsoft.imtalk.utils.displayUrlCyclo
import com.ysxsoft.imtalk.view.BankCardEditActivity

/**
 * 本周明星列表
 */
class WeekAdapter4(mContext:Context) :ListBaseAdapter<StarBean.DataBean>(mContext) {

    private lateinit var listener : OnItemClickListener

    override val layoutId: Int
        get() = R.layout.item_tyrant

    override fun getItemCount(): Int {
        return super.getItemCount()-3
    }

    fun setOnclickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList[position+3]
        //排名
        holder.getView<TextView>(R.id.tvNo)!!.text = (position+4).toString()
        //头像圆形
        holder.getView<ImageView>(R.id.ivAvatar)!!.displayUrlCyclo(bean.icon)
        //姓名
        holder.getView<TextView>(R.id.tvName)!!.text = bean.nickname
        // ID
        holder.getView<TextView>(R.id.tvId)!!.text = "ID："+bean.tt_id
        // 钻
        holder.getView<TextView>(R.id.tvNumb)!!.text = bean.now_level.toString()
        // 数量
        holder.getView<TextView>(R.id.tvSize)!!.text = bean.next_user
        // 距离上一名差距
        holder.getView<TextView>(R.id.tvGap)!!.text = "距离上一名"
        holder.getView<LinearLayout>(R.id.linoutItem)?.setOnClickListener {
            listener.onItemClick(it, position, bean)
        }
    }


    interface OnItemClickListener{
        fun onItemClick(itemView : View, position: Int, item : StarBean.DataBean)
    }

}