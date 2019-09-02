package com.ysxsoft.imtalk.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *Create By 胡
 *on 2019/6/10 0010
 */
object CityFileUtil{
    fun getAssetsFileText(context: Context, fileName:String):String{
        val strBuilder=StringBuilder()
        val assetManager=context.assets
        val bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
        bf.use { strBuilder.append(it.readLine())}
        bf.close()
        return strBuilder.toString()
    }
}
