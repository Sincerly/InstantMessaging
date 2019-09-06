package com.ysxsoft.imtalk.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.Nullable
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import java.lang.ref.WeakReference
import java.util.ArrayList

/**
 * 自定义下划线宽度的tablayout
 * jyg
 */
class IndicatorTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var mTabLayout: TabLayout
    private var mTabList: MutableList<String>? = null
    private var mCustomViewList: MutableList<View>? = null
    private var mSelectIndicatorColor: Int = 0
    private var mSelectTextColor: Int = 0
    private var mUnSelectTextColor: Int = 0
    private var mIndicatorHeight: Int = 0
    private var mIndicatorWidth: Int = 0
    private var mTabMode: Int = 0
    private var mTabRippleColor: Int = R.color.white
    private var mTabTextSize: Int = 0
    private var mContentStart: Boolean = false

    init {
        init(context, attrs)
    }

    fun getTabAt(postision: Int): TabLayout.Tab? {
//        val tabAt = mTabLayout.getTabAt(postision)
        return mTabLayout.getTabAt(postision)
    }

    private fun readAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorTabLayout)
        mSelectIndicatorColor = typedArray.getColor(R.styleable.IndicatorTabLayout_tabIndicatorColor, context.resources.getColor(R.color.colorAccent))
        mUnSelectTextColor = typedArray.getColor(R.styleable.IndicatorTabLayout_tabTextColor, Color.parseColor("#666666"))
        mSelectTextColor = typedArray.getColor(R.styleable.IndicatorTabLayout_tabSelectTextColor, context.resources.getColor(R.color.colorAccent))
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabLayout_tabIndicatorHeight, 1)
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabLayout_tabIndicatorWidth, 0)
        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.IndicatorTabLayout_tabTextSize, 15)
        mContentStart = typedArray.getBoolean(R.styleable.IndicatorTabLayout_tabMContentStart, false)
        mTabMode = typedArray.getInt(R.styleable.IndicatorTabLayout_tab_Mode, 2)
        mTabRippleColor = typedArray.getInt(R.styleable.IndicatorTabLayout_tabRippleColor, mTabRippleColor)
        typedArray.recycle()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        readAttr(context, attrs)

        mTabList = ArrayList()
        mCustomViewList = ArrayList()
        val view = LayoutInflater.from(getContext()).inflate(if (mContentStart) R.layout.enhance_contentstart_tab_layout else R.layout.enhance_tab_layout, this, true)
        mTabLayout = view.findViewById(R.id.enhance_tab_view)
        mTabLayout.tabRippleColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), mTabRippleColor))
        // 添加属性
        mTabLayout.tabMode = if (mTabMode == 1) TabLayout.MODE_FIXED else TabLayout.MODE_SCROLLABLE
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (i in 0 until mTabLayout.tabCount) {
                    val view = mTabLayout.getTabAt(i)?.customView ?: return
                    val text = view.findViewById(R.id.tab_item_text) as TextView
                    val indicator: View = view.findViewById(R.id.tab_item_indicator)
                    if (i == tab.position) { // 选中状态
                        text.setTextColor(mSelectTextColor)
                        indicator.setBackgroundColor(mSelectIndicatorColor)
                        indicator.visibility = View.VISIBLE
                    } else {// 未选中状态
                        text.setTextColor(mUnSelectTextColor)
                        indicator.visibility = View.INVISIBLE
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun getCustomViewList(): List<View>? {
        return mCustomViewList
    }

    fun addOnTabSelectedListener(onTabSelectedListener: TabLayout.OnTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    /**
     * 与TabLayout 联动
     * @param viewPager
     */
    fun setupWithViewPager(@Nullable viewPager: ViewPager) {
        mTabLayout.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(viewPager, this))
    }


    /**
     * retrive TabLayout Instance
     * @return
     */
    fun getTabLayout(): TabLayout {
        return mTabLayout
    }

    /**
     * 添加tab
     * @param tab
     */
    fun addTab(tab: String) {
        mTabList?.add(tab)
        val customView = getTabView(context, tab, mIndicatorWidth, mIndicatorHeight, mTabTextSize)
        mCustomViewList?.add(customView)
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(customView))
    }

    class ViewPagerOnTabSelectedListener(private val mViewPager: ViewPager, enhanceTabLayout: IndicatorTabLayout) : TabLayout.OnTabSelectedListener {
        private val mTabLayoutRef: WeakReference<IndicatorTabLayout>?

        init {
            mTabLayoutRef = WeakReference(enhanceTabLayout)
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            mViewPager.currentItem = tab.position
            val mTabLayout = mTabLayoutRef!!.get()
            val customViewList = mTabLayout?.getCustomViewList()
            if (customViewList == null || customViewList.isEmpty()) {
                return
            }
            for (i in customViewList.indices) {
                val view = customViewList[i]
                val text = view.findViewById(R.id.tab_item_text) as TextView
                val indicator: View = view.findViewById(R.id.tab_item_indicator)
                if (i == tab.position) { // 选中状态
                    text.setTextColor(mTabLayout.mSelectTextColor)
                    indicator.setBackgroundColor(mTabLayout.mSelectIndicatorColor)
                    indicator.visibility = View.VISIBLE
                } else {// 未选中状态
                    text.setTextColor(mTabLayout.mUnSelectTextColor)
                    indicator.visibility = View.INVISIBLE
                }
            }

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // No-op
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // No-op
        }
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param
     * @return
     */
    fun getTabView(context: Context, text: String, indicatorWidth: Int, indicatorHeight: Int, textSize: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null)
        val tabText = view.findViewById(R.id.tab_item_text) as TextView
        if (indicatorWidth > 0) {
            val indicator: View = view.findViewById(R.id.tab_item_indicator)
            val layoutParams = indicator.layoutParams
            layoutParams.width = indicatorWidth
            layoutParams.height = indicatorHeight
            indicator.layoutParams = layoutParams
        }
        tabText.textSize = textSize.toFloat()
        tabText.text = text
        return view
    }
}