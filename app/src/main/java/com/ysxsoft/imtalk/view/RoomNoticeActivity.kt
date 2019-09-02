package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.room_notice_layout.*

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class RoomNoticeActivity:BaseActivity(){
    override fun getLayout(): Int {
        return R.layout.room_notice_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("房间公告")
        setBackVisibily()
        initView()
    }

    private fun initView() {
        ed_room_title.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_0.setText((15 - s!!.length).toString())
            }
        })

        ed_room_content.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv1.setText((300- s!!.length).toString())
            }
        })
        tv_submint.setOnClickListener {
            showToastMessage("提交房间公告")
        }
    }
}