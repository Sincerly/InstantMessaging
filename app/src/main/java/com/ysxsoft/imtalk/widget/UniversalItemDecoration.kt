package com.ysxsoft.imtalk.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.SparseArray
import android.view.View
import com.ysxsoft.imtalk.utils.BaseApplication


abstract class UniversalItemDecoration : RecyclerView.ItemDecoration(){

    private val decorations = SparseArray<Decoration>()

    private val TAG = "UniversalItemDecoration"

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childSize = parent.childCount
        for (i in 0 until childSize) {

            val child = parent.getChildAt(i)
            //获取在getItemOffsets存起来的position
            val position = string2Int(child.tag.toString(), 0)
            val decoration = decorations.get(position) ?: continue

            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            //view的上下左右包括 Margin
            val bottom = child.bottom + layoutParams.bottomMargin
            val left = child.left - layoutParams.leftMargin
            val right = child.right + layoutParams.rightMargin
            val top = child.top - layoutParams.topMargin

            //下面的
            decoration.drawItemOffsets(c, left - decoration.left, bottom, right + decoration.right, bottom + decoration.bottom)
            //上面的
            decoration.drawItemOffsets(c, left - decoration.left, top - decoration.top, right + decoration.right, top)
            //左边的
            decoration.drawItemOffsets(c, left - decoration.left, top, left, bottom)
            //右边的
            decoration.drawItemOffsets(c, right, top, right + decoration.right, bottom)

        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        //获取position
        val position = parent.getChildAdapterPosition(view)
        view.setTag(position)

        //获取调用者返回的Decoration
        var decoration: Decoration? = getItemOffsets(position)

        if (decoration != null) {
            //偏移量设置给item
            outRect.set(decoration.left, decoration.top, decoration.right, decoration.bottom)

        } else {
            //不要线
            decoration = null
        }
        //存起来在onDraw用
        decorations.put(position, decoration!!)

    }


    /***
     * 需调用者返回分割线对象  上下左右 和颜色值
     * @param position
     * @return
     */
    abstract fun getItemOffsets(position: Int): Decoration

    /**
     * 分割线
     */
    abstract class Decoration {

        var left: Int = 0
        var right: Int = 0
        var top: Int = 0
        var bottom: Int = 0

        /**
         * 根据偏移量设定的 当前的线在界面中的坐标
         *
         * @param leftZ
         * @param topZ
         * @param rightZ
         * @param bottomZ
         */
        abstract fun drawItemOffsets(c: Canvas, leftZ: Int, topZ: Int, rightZ: Int, bottomZ: Int)

    }

    class ColorDecoration : Decoration {

        private var mPaint: Paint? = null
        var decorationColor = Color.BLACK

        constructor(color: Int) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint?.style = Paint.Style.FILL
            decorationColor = ContextCompat.getColor(BaseApplication.mContext!!, color)
        }

        constructor() {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint?.style = Paint.Style.FILL
        }

        override fun drawItemOffsets(c: Canvas, leftZ: Int, topZ: Int, rightZ: Int, bottomZ: Int) {

            mPaint?.color = decorationColor
            c.drawRect(leftZ.toFloat(), topZ.toFloat(), rightZ.toFloat(), bottomZ.toFloat(), mPaint)
        }

    }


    private fun string2Int(s: String, defValue: Int): Int {
        return try {
            Integer.parseInt(s)
        } catch (e: Exception) {
            defValue
        }
    }
}