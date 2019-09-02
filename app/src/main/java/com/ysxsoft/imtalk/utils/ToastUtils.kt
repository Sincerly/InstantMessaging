package com.ysxsoft.imtalk.utils

import android.content.Context
import android.widget.Toast

/**
 * Create By 胡
 * on 2019/7/13 0013
 */
object ToastUtils{
    fun showToast(mContext: Context,str:String){
        Toast.makeText(mContext,str,Toast.LENGTH_LONG).show()
    }
     fun showResToast(mContext: Context,res:Int){
         showToast(mContext,res.toString())
    }

}
