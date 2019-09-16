package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMicListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/8/5 0005
 */
class MicPostionAdapter(mContext: Context) : ListBaseAdapter<RoomMicListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.mic_postion_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideHeadImageLoad(mContext, bean.icon, holder.getView<CircleImageView>(R.id.img_head)!!)
        if (position == 0) holder.getView<TextView>(R.id.tv_NO)!!.setText("房主") else holder.getView<TextView>(R.id.tv_NO)!!.setText(bean.sort.toString())
//        if (bean.isChoosed) {
//            holder.getView<ImageView>(R.id.img_cir_bg)!!.visibility= VISIBLE
//            holder.getView<ImageView>(R.id.img_cir_bg)!!.setBackgroundResource(R.drawable.btn_cir_bg)
//        }else{
//            holder.getView<ImageView>(R.id.img_cir_bg)!!.visibility=GONE
//        }

//        var cb_box = holder.getView<CheckBox>(R.id.cb_box)
//
//        if (bean.isChoosed()) {
//            cb_box!!.setChecked(true)
//        } else {
//            cb_box!!.setChecked(false)
//        }
//        cb_box.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                checkInterface!!.checkGroup(position, isChecked)
//            }
//        })

        var header=holder.getView<TextView>(R.id.header);
        header!!.isSelected=bean.isChoosed;
        header.setOnClickListener(View.OnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position)
            }
        })
//        cb_box.setOnClickListener(View.OnClickListener { v ->
//            bean.setChoosed((v as CheckBox).isChecked)
//            if (checkInterface != null) {
//                checkInterface!!.checkGroup(position, v.isChecked)
//            }
//        })


    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    private var onClickListener: OnClickListener? = null
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
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

    fun setSelect(position: Int) {
        notifyDataSetChanged()
    }

    /**
     * 复选框接口
     */
    interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        fun checkGroup(position: Int, isChecked: Boolean)
    }
}