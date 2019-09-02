package com.ysxsoft.imtalk.bean

import com.google.gson.annotations.SerializedName

import org.litepal.crud.LitePalSupport

/**
 * Create By 胡
 * on 2019/8/16 0016
 */
class UserInfo : LitePalSupport() {

    var nikeName: String? = null
    var icon: String? = null
    var sex: String? = null
    var zsl: String? = null
    var uid: String? = null
}
