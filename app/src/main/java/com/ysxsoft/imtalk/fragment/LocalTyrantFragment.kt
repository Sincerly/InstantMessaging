package com.ysxsoft.imtalk.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment

private const val ARG_POSITION = "position"

/**
 * 土豪 日/周/月 榜
 */
class LocalTyrantFragment : BaseFragment() {

    var position = 0

    override fun getLayoutResId(): Int {
        return R.layout.fragment_local_tyrant
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        position =  arguments?.getInt(ARG_POSITION) ?: position
    }














    companion object {
        /**
         * set fragment position
         */
        @JvmStatic
        fun newInstance(param1: Int) =
                LocalTyrantFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, param1)
                    }
                }
    }
}
