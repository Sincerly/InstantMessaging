package com.ysxsoft.imtalk.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ysxsoft.imtalk.R

object ImageLoadUtil {

    /**
     * 加载头像
     * @param context
     * @param url
     * @param view
     */
    fun GlideHeadImageLoad(context: Context, url: String, view: ImageView) {
        val options = RequestOptions().placeholder(R.mipmap.img_normal_head).error(R.mipmap.img_normal_head)
        Glide.with(context).load(url).apply(options).into(view)
    }

    /**
     * 加载商品图片
     */
    fun GlideGoodsImageLoad(context: Context, url: String, view: ImageView) {
        //        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default).error(R.mipmap.img_default);
        Glide.with(context).load(url)/*.apply(options)*/.into(view)
    }
}