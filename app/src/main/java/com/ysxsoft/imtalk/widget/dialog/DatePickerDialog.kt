package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.widget.DatePicker
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.dialog_datepicker.*
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog(var mContext: Context) : ABSDialog(mContext) {
    var dateStr: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        var calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), object : DatePicker.OnDateChangedListener {
            override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                dateStr = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            }
        })
        tv_ok.setOnClickListener {
            dismiss()
            if (TextUtils.isEmpty(dateStr)) {
                val df = SimpleDateFormat("yyyy-MM-dd")//设置日期格式
                val format = df.format(Date())  // new Date()为获取当前系统时间
                if (clickDatePicker != null) {
                    clickDatePicker!!.datePicker(format)
                }
            } else {
                if (clickDatePicker != null) {
                    clickDatePicker!!.datePicker(dateStr!!)
                }
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.dialog_datepicker
    }

    interface ClickDatePicker {
        fun datePicker(date: String)
    }

    private var clickDatePicker: ClickDatePicker? = null
    fun setClickDatePicker(clickDatePicker: ClickDatePicker) {
        this.clickDatePicker = clickDatePicker
    }
}