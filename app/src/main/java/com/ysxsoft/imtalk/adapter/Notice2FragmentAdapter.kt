package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.SysBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/8/15 0015
 */
class Notice2FragmentAdapter(mContext: Context):ListBaseAdapter<SysBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.notice2_fragment_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_time)!!.setText(bean.add_time)
        holder.getView<TextView>(R.id.tv_sys_title)!!.setText(bean.sys_title)
        holder.getView<TextView>(R.id.tv_desc)!!.setText(bean.sys_desc)

    }
}