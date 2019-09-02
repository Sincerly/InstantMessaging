package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.HelpBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.view.WebHelpActivity

/**
 *Create By 胡
 *on 2019/7/24 0024
 */
class HelpAdapter(mContext: Context):ListBaseAdapter<HelpBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.activity_item_help

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv1)!!.setText(bean.title)
        holder.getView<TextView>(R.id.tv1)!!.setOnClickListener {
            WebHelpActivity.starWebHelpActivity(mContext,bean.title,bean.id.toString())
        }
    }
}