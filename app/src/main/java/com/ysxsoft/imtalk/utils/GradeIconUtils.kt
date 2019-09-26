package com.ysxsoft.imtalk.utils

import com.ysxsoft.imtalk.R

class GradeIconUtils {


    companion object{
        fun charmIcon( grade : Int) : IntArray{
            val array = intArrayOf(0,0)
            when(grade){
                in 0..9 ->{
                    array[0] = R.mipmap.icon_m_1_9
                    array[1] = R.color.colorM_1_9
                }
                in 10..19 ->{
                    array[0] = R.mipmap.icon_m_10_19
                    array[1] = R.color.colorM_10_19
                }
                in 20..29 ->{
                    array[0] = R.mipmap.icon_m_20_29
                    array[1] = R.color.colorM_20_29
                }
                in 30..39 ->{
                    array[0] = R.mipmap.icon_m_30_39
                    array[1] = R.color.colorM_30_39
                }
                in 40..49 ->{
                    array[0] = R.mipmap.icon_m_40_49
                    array[1] = R.color.colorM_40_49
                }
                in 50..59 ->{
                    array[0] = R.mipmap.icon_m_50_59
                    array[1] = R.color.colorM_50_59
                }
                in 60..69 ->{
                    array[0] = R.mipmap.icon_m_60_69
                    array[1] = R.color.colorM_60_69
                }
                in 70..79 ->{
                    array[0] = R.mipmap.icon_m_70_79
                    array[1] = R.color.colorM_70_79
                }
                in 80..89 ->{
                    array[0] = R.mipmap.icon_m_80_89
                    array[1] = R.color.colorM_80_89
                }
                in 90..100 ->{
                    array[0] = R.mipmap.icon_m_90_100
                    array[1] = R.color.colorM_90_100
                }
            }
            return array
        }
        fun gradeIcon(grade : Int) : IntArray{
            val array = intArrayOf(0,0)
            when(grade){
                in 0..9 ->{
                    array[0] = R.mipmap.icon_1_9
                    array[1] = R.color.color_1_9
                }
                in 10..19 ->{
                    array[0] = R.mipmap.icon_10_19
                    array[1] = R.color.color_10_19
                }
                in 20..29 ->{
                    array[0] = R.mipmap.icon_20_29
                    array[1] = R.color.color_20_29
                }
                in 30..39 ->{
                    array[0] = R.mipmap.icon_30_39
                    array[1] = R.color.color_30_39
                }
                in 40..49 ->{
                    array[0] = R.mipmap.icon_40_49
                    array[1] = R.color.color_40_49
                }
                in 50..59 ->{
                    array[0] = R.mipmap.icon_50_59
                    array[1] = R.color.color_50_59
                }
                in 60..69 ->{
                    array[0] = R.mipmap.icon_60_69
                    array[1] = R.color.color_60_69
                }
                in 70..79 ->{
                    array[0] = R.mipmap.icon_70_79
                    array[1] = R.color.color_70_79
                }
                in 80..89 ->{
                    array[0] = R.mipmap.icon_80_89
                    array[1] = R.color.color_80_89
                }
                in 90..99 ->{
                    array[0] = R.mipmap.icon_90_99
                    array[1] = R.color.color_90_99
                }
                in 100..149 ->{
                    array[0] = R.mipmap.icon_100_149
                    array[1] = R.color.color_100_149
                }
                in 150..199 ->{
                    array[0] = R.mipmap.icon_150_199
                    array[1] = R.color.color_150_199
                }
                in 200..249 ->{
                    array[0] = R.mipmap.icon_200_249
                    array[1] = R.color.color_200_249
                }
                in 250..299 ->{
                    array[0] = R.mipmap.icon_250_299
                    array[1] = R.color.color_250_299
                }
                in 300..399 ->{
                    array[0] = R.mipmap.icon_300_399
                    array[1] = R.color.color_300_399
                }
                in 400..500 ->{
                    array[0] = R.mipmap.icon_400_500
                    array[1] = R.color.color_400_500
                }
            }
            return array
        }
    }
}