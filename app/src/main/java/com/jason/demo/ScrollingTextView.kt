package com.jason.demo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

class ScrollingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private var scrollAnimator: ValueAnimator? = null
    private var scrollOffset = 0f
    private var isScrolling = false
    private var maxScrollDistance = 0f
    private var scrollDuration: Long = -1 // 滚动时长（毫秒），-1 表示使用默认计算方式

    init {
        isSingleLine = true
        ellipsize = null
    }

    /**
     * 设置滚动时长
     * @param duration 滚动时长（毫秒），如果设置为 -1 或小于 0，则使用默认计算方式（50像素/秒）
     */
    fun setScrollDuration(duration: Long) {
        scrollDuration = if (duration < 0) -1 else duration
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        post {
            checkIfNeedScroll()
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        post {
            checkIfNeedScroll()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        post {
            checkIfNeedScroll()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // 先绘制背景（背景不滚动，保持在原位）
        background?.draw(canvas)
        
        if (text == null || text.isEmpty()) {
            return
        }

        // 保存 Canvas 状态
        canvas.save()
        
        // 设置裁剪区域，限制文字只在 TextView 的可见区域内显示
        val clipLeft = paddingLeft.toFloat()
        val clipRight = (width - paddingRight).toFloat()
        val clipTop = paddingTop.toFloat()
        val clipBottom = (height - paddingBottom).toFloat()
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom)

        // 直接使用 Paint 绘制文字
        val textStr = text.toString()
        val paint = paint
        paint.color = currentTextColor
        paint.textSize = textSize
        paint.isAntiAlias = true
        
        // 计算文字绘制的垂直位置（垂直居中）
        val fontMetrics = paint.fontMetrics
        val textY = (height / 2f) - ((fontMetrics.ascent + fontMetrics.descent) / 2f)
        
        // 计算文字绘制的水平起始位置
        val textStartX = paddingLeft.toFloat()
        
        // 如果有滚动偏移，调整文字起始位置（向左移动）
        val drawX = if (scrollOffset > 0) {
            textStartX - scrollOffset
        } else {
            textStartX
        }
        
        // 在裁剪区域内绘制文字
        canvas.drawText(textStr, drawX, textY, paint)
        
        // 恢复 Canvas 状态
        canvas.restore()
    }

    private fun checkIfNeedScroll() {
        if (text == null || text.isEmpty()) {
            resetScroll()
            return
        }

        val paint = paint
        val textWidth = paint.measureText(text.toString())
        val viewWidth = width - paddingLeft - paddingRight

        if (textWidth > viewWidth) {
            val newMaxScrollDistance = textWidth - viewWidth
            // 如果滚动距离变化了，或者当前没有在滚动，重新开始滚动
            if (!isScrolling || maxScrollDistance != newMaxScrollDistance) {
                startScroll(newMaxScrollDistance)
            }
        } else {
            resetScroll()
        }
    }

    private fun startScroll(maxScrollDistance: Float) {
        stopScroll()

        this.maxScrollDistance = maxScrollDistance
        isScrolling = true
        scrollOffset = 0f

        scrollAnimator = ValueAnimator.ofFloat(0f, maxScrollDistance).apply {
            // 如果设置了自定义时长，使用自定义时长；否则使用默认计算方式（50像素/秒）
            duration = if (scrollDuration > 0) {
                scrollDuration
            } else {
                (maxScrollDistance / 50 * 1000).toLong() // 默认速度：50像素/秒
            }
            addUpdateListener { animation ->
                scrollOffset = animation.animatedValue as Float
                invalidate()
            }
            addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: android.animation.Animator) {}
                override fun onAnimationCancel(animation: android.animation.Animator) {}
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    // 动画完成后，保持最终位置，但标记为不再滚动
                    isScrolling = false
                    scrollOffset = maxScrollDistance
                    invalidate()
                }
            })
            start()
        }
    }

    private fun stopScroll() {
        scrollAnimator?.cancel()
        scrollAnimator = null
    }

    private fun resetScroll() {
        stopScroll()
        isScrolling = false
        scrollOffset = 0f
        maxScrollDistance = 0f
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetScroll()
    }
}

