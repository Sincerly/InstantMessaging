package com.ysxsoft.imtalk.adapter.fgpageradapter

import android.support.v4.app.Fragment

class FgTableBean< T : Fragment>(fragment: T, title: String, id: Int) {

    private var fragment: T? = fragment
    private var title: String? = title
    private var id: Int = id

    fun getFragment(): T? {
        return fragment
    }

    fun setFragment(fragment: T) {
        this.fragment = fragment
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }
}