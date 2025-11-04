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

    init {
        isSingleLine = true
        ellipsize = null
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

        // 直接使用 Paint 绘制文字，这样可以完全控制绘制区域
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
        
        // 直接绘制文字，不受 TextView 边界限制
        // 父容器已设置 clipChildren="false"，所以文字可以超出边界显示
        canvas.drawText(textStr, drawX, textY, paint)
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
            duration = (maxScrollDistance / 50 * 1000).toLong() // 控制滚动速度，50像素/秒
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

