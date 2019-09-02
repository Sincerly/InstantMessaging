package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.gif_dialog.*

/**
 *Create By 胡
 *on 2019/8/27 0027
 */
class GifDialog(var mContext: Context, var gifurl: String?):ABSDialog(mContext){

    override fun initView() {
//        gif_view
        ImageLoadUtil.GlideGoodsImageLoad(mContext,gifurl!!,gif_view)
    }

    override fun getLayoutResId(): Int {
        return R.layout.gif_dialog
    }
}