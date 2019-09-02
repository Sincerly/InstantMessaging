package com.ysxsoft.imtalk.com

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * 描述： TODO
 * 日期： 2018/11/13 0013 13:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class SuperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views: SparseArray<View>

    init {
        this.views = SparseArray()
    }

    fun <T : View> getView(viewId: Int): T? {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T?
    }
}
