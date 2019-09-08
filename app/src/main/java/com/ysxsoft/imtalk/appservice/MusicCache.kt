package com.ysxsoft.imtalk.appservice

import org.litepal.crud.LitePalSupport

/**
 * create by Sincerly on 2019/3/2 0002
 */
class MusicCache : LitePalSupport() {
    var id: Long = 0
    var uid: String? = null
    var webPath: String? = null
    var nativePath: String? = null
    var mid: String? = null
}
