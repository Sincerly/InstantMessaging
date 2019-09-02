package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ReportListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/7/30 0030
 */
class ReportAdapter(mContext: Context):ListBaseAdapter<ReportListBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.report_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<CheckBox>(R.id.cb_box)!!.setText(bean.title)

        if (bean.isChoosed()) {
            holder.getView<CheckBox>(R.id.cb_box)!!.setChecked(true)
        } else {
            holder.getView<CheckBox>(R.id.cb_box)!!.setChecked(false)
        }
        holder.getView<CheckBox>(R.id.cb_box)!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                bean.setChoosed((v as CheckBox).isChecked);
                if (checkInterface != null) {
                    checkInterface!!.checkGroup(position, (v as CheckBox).isChecked)
                }
            }
        })
    }



    interface CheckInterface {
        fun checkGroup(position: Int, isChecked: Boolean)
    }

    private var checkInterface: CheckInterface? = null
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    fun setCheckInterface(checkInterface: CheckInterface) {
        this.checkInterface = checkInterface
    }
}