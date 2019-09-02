package com.ysxsoft.imtalk.im

import android.content.Context
import android.content.Intent
import io.rong.push.PushType
import io.rong.push.notification.PushMessageReceiver
import io.rong.push.notification.PushNotificationMessage

/**
 *Create By 胡
 *on 2019/8/10 0010
 */
class SealNotificationReceiver : PushMessageReceiver(){
    override fun onNotificationMessageClicked(p0: Context?, p1: PushType?, p2: PushNotificationMessage?): Boolean {
        return false // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。

    }

    override fun onNotificationMessageArrived(p0: Context?, p1: PushType?, p2: PushNotificationMessage?): Boolean {
        return false // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }
}