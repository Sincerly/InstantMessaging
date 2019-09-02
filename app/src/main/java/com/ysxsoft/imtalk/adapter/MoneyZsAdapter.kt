package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RecordDetailBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/8/12 0012
 */
class MoneyZsAdapter(mContext: Context):ListBaseAdapter<RecordDetailBean.DataBean.RecordListBean>(mContext){
    override val layoutId: Int
        get() = R.layout.fm_money_zs_item

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_type)!!.setText(bean.title)
        holder.getView<TextView>(R.id.tv_time)!!.setText(bean.add_time)
        holder.getView<TextView>(R.id.tv_num)!!.setText(bean.money)
    }
}