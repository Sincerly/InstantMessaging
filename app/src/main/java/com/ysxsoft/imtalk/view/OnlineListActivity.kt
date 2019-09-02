package com.ysxsoft.imtalk.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.displayResCyclo
import kotlinx.android.synthetic.main.activity_online_list.*
import kotlinx.android.synthetic.main.include_onlyrecyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_toolbar.toolBar
import kotlinx.android.synthetic.main.include_toolbar.tvTitle

class OnlineListActivity : BaseActivity() {


    private lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>

    override fun getLayout(): Int {
        return R.layout.activity_online_list
    }


    override fun initUi() {
        initToolBar(viewTop)
        setStatusBarFullTransparent()
        tvTitle.text = "在线人数（666）"
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        initAdapter()
    }

    private fun initAdapter() {
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_online, arrayOfNulls<String>(10).toList()) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.getView<ImageView>(R.id.ivAvatar)?.displayResCyclo(R.mipmap.icon_default_avatar)
                val show = if (helper?.adapterPosition ?: 0 == 0) View.GONE else View.VISIBLE
                helper?.getView<View>(R.id.viewLines)?.visibility = show
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            showToastMessage(position.toString())
        }
    }

}
