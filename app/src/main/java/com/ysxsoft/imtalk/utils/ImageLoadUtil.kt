package com.ysxsoft.imtalk.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

        var roundedCorners = RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        var options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(context).load(url).apply(options).into(view)
    }
}