package com.jason.demo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.jason.demo.R

class ViewPagerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var indicatorCount = 4
    private var indicatorSize = 8
    private var indicatorSpacing = 8
    private var indicatorColor = context.resources.getColor(R.color.gray)
    private var indicatorSelectedColor = context.resources.getColor(R.color.purple_500)
    private var indicators = mutableListOf<View>()
    private var currentPosition = 0

    init {
        // 从属性中获取配置
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ViewPagerIndicator)
            indicatorCount = typedArray.getInt(R.styleable.ViewPagerIndicator_indicatorCount, 4)
            indicatorSize = typedArray.getDimensionPixelSize(R.styleable.ViewPagerIndicator_indicatorSize, 8)
            indicatorSpacing = typedArray.getDimensionPixelSize(R.styleable.ViewPagerIndicator_indicatorSpacing, 8)
            indicatorColor = typedArray.getColor(R.styleable.ViewPagerIndicator_indicatorColor, indicatorColor)
            indicatorSelectedColor = typedArray.getColor(R.styleable.ViewPagerIndicator_indicatorSelectedColor, indicatorSelectedColor)
            typedArray.recycle()
        }

        orientation = HORIZONTAL
        createIndicators()
    }

    private fun createIndicators() {
        removeAllViews()
        indicators.clear()

        for (i in 0 until indicatorCount) {
            val indicator = LayoutInflater.from(context).inflate(R.layout.item_indicator, this, false)
            val params = LayoutParams(indicatorSize, indicatorSize)
            if (i > 0) {
                params.leftMargin = indicatorSpacing
            }
            indicator.layoutParams = params
            indicator.setBackgroundColor(if (i == currentPosition) indicatorSelectedColor else indicatorColor)
            addView(indicator)
            indicators.add(indicator)
        }
    }

    fun setViewPager(viewPager: ViewPager2, count: Int) {
        indicatorCount = count
        createIndicators()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val realPosition = position % count
                setCurrentItem(realPosition)
            }
        })
    }

    fun setCurrentItem(position: Int) {
        if (position < 0 || position >= indicatorCount) return

        indicators[currentPosition].setBackgroundColor(indicatorColor)
        indicators[position].setBackgroundColor(indicatorSelectedColor)
        currentPosition = position
    }
}
