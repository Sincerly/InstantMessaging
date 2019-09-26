package com.ysxsoft.imtalk.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.rong.imkit.fragment.ConversationFragment
import android.text.TextUtils
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.PalMessageBus
import com.ysxsoft.imtalk.bean.RealInfoBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.im.message.LobbyTextMessage
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.BindPhoneActivity
import com.ysxsoft.imtalk.view.SmrzActivity
import com.ysxsoft.imtalk.widget.dialog.BindingPhoneDialog
import io.rong.common.rlog.RLog
import io.rong.imkit.InputBar
import io.rong.imkit.RongExtension
import io.rong.imkit.RongIM
import io.rong.imkit.mention.RongMentionManager
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import org.litepal.extension.find
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


/**
 * 交友大厅
 */

class PalLobbyFragment : ConversationFragment() {


    private var code : Int = -1
    private lateinit var bottomBar: RongExtension
    private lateinit var sendToggleBtn: FrameLayout
    private lateinit var info: UserInfoBean.DataBean


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        info = Gson().fromJson(SharedPreferencesUtils.getInfo(activity as Context), UserInfoBean.DataBean::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vv = super.onCreateView(inflater, container, savedInstanceState)

        val paddingx = AppUtil.dip2px(MyApplication.mcontext, 16)
        val paddingxx = AppUtil.dip2px(MyApplication.mcontext, 8)
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
            params.marginStart = paddingxx
            params.marginEnd = paddingxx
            bottomBar.inputEditText.layoutParams = params
        }
        sendToggleBtn.post {
            val params = sendToggleBtn.layoutParams as LinearLayout.LayoutParams
            params.width = AppUtil.dip2px(MyApplication.mcontext, 60)
            params.height = AppUtil.dip2px(MyApplication.mcontext, 34)
            params.marginStart = paddingy
            params.marginEnd = paddingxx
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
        if (sendToggleBtn.visibility == View.GONE || sendToggleBtn.visibility == View.INVISIBLE) sendToggleBtn.visibility = View.VISIBLE
    }

    private fun getCode(listener : CodeListener) {
        if ( code == 0){
            listener.getCode(code)
            return
        }
        NetWork.getService(ImpService::class.java)
                .getRealInfo(SpUtils.getSp(activity as Context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<RealInfoBean> {
                    override fun call(t: RealInfoBean?) {
                        if ("0".equals(t!!.code)) {
                            code = t.data.is_real
                            listener.getCode(code)
                        }
                    }
                })
    }

    /**
     * 发送信息
     */
//    使用自定义消息LobbyTextMessage同时使用自定义消息模板LobbyTextMessageProvider，
//    因为uimessage的问题表情只能自动使用android表情，需要在provider内部自己转换，不知道会不会存在弊端
    override fun onSendToggleClick(v: View, text: String) {
        getCode(object : CodeListener{
            override fun getCode(code: Int) {
                when (code) {
                    0 -> {
                        val bindingPhoneDialog = BindingPhoneDialog(activity as Context)
                        val tv_tip = bindingPhoneDialog.findViewById<TextView>(R.id.tv_tip)
                        val tv_go = bindingPhoneDialog.findViewById<TextView>(R.id.tv_go)
                        tv_tip.setText("请先实名认证")
                        tv_go.setOnClickListener {
                            startActivity(Intent(activity, SmrzActivity::class.java))
                        }
                        bindingPhoneDialog.show()
                    }
                    else ->{
                        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim { it <= ' ' })) {
                            val textMessage = LobbyTextMessage.obtain(text)
                            val mentionedInfo = RongMentionManager.getInstance().onSendButtonClick()
                            if (mentionedInfo != null) {
                                textMessage.mentionedInfo = mentionedInfo
                            }
                            //添加自定义信息extra
                            textMessage.extra = SharedPreferencesUtils.getInfo(activity as Context)
                            val message = Message.obtain(targetId, conversationType, textMessage)
                            RongIM.getInstance().sendMessage(message, null as String?, null as String?, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {
                                }

                                override fun onSuccess(p0: Message?) {
                                    EventBus.getDefault().post(PalMessageBus(p0))
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                }

                            })
                        } else {
                            RLog.e("ConversationFragment", "text content must not be null")
                        }
                    }
                }
            }
        })
    }
//    //使用系统消息TextMessage，只注册自定义模板LobbyMessageProvider，弊端需要在透传信息extra里做标识，然后再LobbyMessageProvider里判断
//    //根据不同状况显示不同页面，模板较为复杂，如果页面出现多种气泡样式可以注释掉这里使用上边的自定义消息同时自定义模板
//    override fun onSendToggleClick(v: View?, text: String?) {
//        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text?.trim { it <= ' ' })) {
//            val textMessage = TextMessage.obtain(text)
//            val mentionedInfo = RongMentionManager.getInstance().onSendButtonClick()
//            if (mentionedInfo != null) {
//                textMessage.mentionedInfo = mentionedInfo
//            }
//            //添加自定义信息extra
//            val jsonObject = JsonObject()
//            jsonObject.addProperty("uid", info.uid)
//            jsonObject.addProperty("icon", info.icon)
//            jsonObject.addProperty("nikeName", info.nikeName)
//            jsonObject.addProperty("sex", info.sex)
//            jsonObject.addProperty("zsl", info.zsl)//钻数量
//            textMessage.extra = jsonObject.toString()
//            val message = Message.obtain(targetId, conversationType, textMessage)
//            RongIM.getInstance().sendMessage(message, null as String?, null as String?, null as IRongCallback.ISendMessageCallback?)
//        } else {
//            RLog.e("ConversationFragment", "text content must not be null")
//        }
//    }

    interface CodeListener{
        fun  getCode(code : Int)
    }
}