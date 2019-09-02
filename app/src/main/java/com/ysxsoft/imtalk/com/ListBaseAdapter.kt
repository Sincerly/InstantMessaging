package com.ysxsoft.imtalk.com

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * 描述： TODO
 * 日期： 2018/11/13 0013 13:45
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
abstract class ListBaseAdapter<T>(protected var mContext: Context) : RecyclerView.Adapter<SuperViewHolder>() {
    private val mInflater: LayoutInflater

    protected var mDataList: MutableList<T> = ArrayList()

    abstract val layoutId: Int

    val dataList: List<T>
        get() = mDataList

    init {
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder {
        val itemView = mInflater.inflate(layoutId, parent, false)
        return SuperViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SuperViewHolder, position: Int) {
        onBindItemHolder(holder, position)
    }

    //局部刷新关键：带payload的这个onBindViewHolder方法必须实现
    override fun onBindViewHolder(holder: SuperViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            onBindItemHolder(holder, position, payloads)
        }

    }

    abstract fun onBindItemHolder(holder: SuperViewHolder, position: Int)

    fun onBindItemHolder(holder: SuperViewHolder, position: Int, payloads: List<Any>) {

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setDataList(list: Collection<T>) {
        this.mDataList.clear()
        this.mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun addAll(list: Collection<T>) {
        val lastIndex = this.mDataList.size
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size)
        }
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        this.mDataList.removeAt(position)
        notifyItemRemoved(position)

        if (position != dataList.size) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.mDataList.size - position)
        }
    }

    fun clear() {
        mDataList.clear()
        notifyDataSetChanged()
    }


}
