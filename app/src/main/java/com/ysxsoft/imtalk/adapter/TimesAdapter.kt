package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import org.w3c.dom.Text

/**
 *Create By 胡
 *on 2019/7/28 0028
 */
class TimesAdapter(mContext: Context) : ListBaseAdapter<AwardListDataBean.DataBean.ZjdTimesBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.time_item_layout

    var click = -1
    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv1)!!.setText(bean.title)
       if ( click == position){
           holder.getView<TextView>(R.id.tv1)!!.isSelected=true
       }else{
           holder.getView<TextView>(R.id.tv1)!!.isSelected=false
       }

        holder.getView<TextView>(R.id.tv1)!!.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setSelect(position: Int) {
        click = position
        notifyDataSetChanged()
    }

}
