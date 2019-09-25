package com.ysxsoft.imtalk.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.rong.imkit.fragment.ConversationFragment
import android.text.TextUtils
import android.widget.*
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.im.message.LobbyTextMessage
import com.ysxsoft.imtalk.utils.AppUtil
import io.rong.common.rlog.RLog
import io.rong.imkit.InputBar
import io.rong.imkit.RongExtension
import io.rong.imkit.RongIM
import io.rong.imkit.mention.RongMentionManager
import io.rong.imlib.IRongCallback
import io.rong.imlib.model.Message
import io.rong.message.TextMessage


/**
 * 交友大厅
 */

class PalLobbyFragment : ConversationFragment() {

    private lateinit var bottomBar : RongExtension
    private lateinit var sendToggleBtn : FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vv = super.onCreateView(inflater, container, savedInstanceState)
        val paddingx = AppUtil.dip2px(MyApplication.mcontext, 16)
        val paddingy = AppUtil.dip2px(MyApplication.mcontext, 4)

        bottomBar = vv?.findViewById(io.rong.imkit.R.id.rc_extension)!!
        val layoutJia = bottomBar.findViewById<LinearLayout>(R.id.rc_plugin_layout)
        sendToggleBtn = bottomBar.findViewById(R.id.rc_send_toggle)
        bottomBar.inputEditText.setBackgroundResource(R.drawable.shape_stroke_colorcc_oval_1dp)
        bottomBar.inputEditText.setPadding(paddingx, paddingy, paddingx, paddingy)
        bottomBar.setInputBarStyle(InputBar.Style.STYLE_CONTAINER)
        sendToggleBtn.setBackgroundResource(R.drawable.shape_gradient_oval)
        sendToggleBtn.visibility = View.VISIBLE

        bottomBar.inputEditText.post {
            val params = bottomBar.inputEditText.layoutParams as RelativeLayout.LayoutParams
            params.marginStart = paddingy
            params.marginEnd = paddingy
            bottomBar.inputEditText.layoutParams = params
        }
        sendToggleBtn.post {
            val params = sendToggleBtn.layoutParams as LinearLayout.LayoutParams
            params.width = AppUtil.dip2px(MyApplication.mcontext, 60)
            params.height = AppUtil.dip2px(MyApplication.mcontext, 34)
            params.marginStart = paddingx
            params.marginEnd = paddingx
            sendToggleBtn.layoutParams = params
        }
        layoutJia.post {
            val params = sendToggleBtn.layoutParams as LinearLayout.LayoutParams
            params.marginStart = 0
            params.marginEnd = 0
            layoutJia.layoutParams = params
        }
        return vv
    }

    override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        if ( sendToggleBtn.visibility == View.GONE ||  sendToggleBtn.visibility == View.INVISIBLE ) sendToggleBtn.visibility = View.VISIBLE
    }

    /**
     * 发送信息
     */
    override fun onSendToggleClick(v: View, text: String) {
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim { it <= ' ' })) {
            val textMessage = LobbyTextMessage.obtain(text)
            val mentionedInfo = RongMentionManager.getInstance().onSendButtonClick()
            if (mentionedInfo != null) {
                textMessage.mentionedInfo = mentionedInfo
            }

            val message = Message.obtain(targetId, conversationType, textMessage)
            RongIM.getInstance().sendMessage(message, null as String?, null as String?, null as IRongCallback.ISendMessageCallback?)
        } else {
            RLog.e("ConversationFragment", "text content must not be null")
        }
    }
}