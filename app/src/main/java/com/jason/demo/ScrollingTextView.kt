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
        setClipToPadding(false)
        setClipChildren(false)
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
        super.onDrawBackground(canvas)
        
        if (scrollOffset > 0 && text != null && text.isNotEmpty()) {
            canvas.save()
            // 扩展可绘制区域，允许文字超出右边界显示
            // clipRect 的右边界扩展到足够显示滚动后的文字
            val clipLeft = paddingLeft.toFloat()
            val clipRight = (width - paddingRight + scrollOffset).toFloat()
            val clipTop = paddingTop.toFloat()
            val clipBottom = (height - paddingBottom).toFloat()
            canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom)
            // 向左平移画布，使文字向左滚动
            canvas.translate(-scrollOffset, 0f)
            // 绘制文字内容
            super.onDraw(canvas)
            canvas.restore()
        } else {
            super.onDraw(canvas)
        }
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

