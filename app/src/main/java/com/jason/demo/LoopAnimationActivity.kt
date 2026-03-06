package com.jason.demo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LoopAnimationActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    // 定义三个 ImageView 的初始尺寸（dp）
    private val initialSizes = listOf(56, 40, 32)
    // 定义三个 ImageView 的初始透明度
    private val initialAlphas = listOf(1f, 0.5f, 0.2f)
    // 定义三个 ImageView 的初始左边距（dp）
    private val initialMargins = listOf(0, 24, 48)
    // 当前每个 ImageView 对应的初始状态索引
    private var currentIndices = listOf(0, 1, 2)
    // 动画执行次数，用于控制循环逻辑
    private var animationCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_animation)

        btnStart = findViewById(R.id.btn_start)
        imageView1 = findViewById(R.id.image_view1)
        imageView2 = findViewById(R.id.image_view2)
        imageView3 = findViewById(R.id.image_view3)

        btnStart.setOnClickListener {
            startAnimations()
        }
    }

    private fun startAnimations() {
        // 获取当前每个 ImageView 对应的初始状态索引
        val index1 = currentIndices[0]
        val index2 = currentIndices[1]
        val index3 = currentIndices[2]

        // 第一个 ImageView 动画：向左移动 24dp，放大到 64dp，透明度从对应初始状态到 0
        val translate1 = ObjectAnimator.ofFloat(imageView1, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX1 = ObjectAnimator.ofFloat(imageView1, View.SCALE_X, 1f, 64f / initialSizes[index1])
        val scaleY1 = ObjectAnimator.ofFloat(imageView1, View.SCALE_Y, 1f, 64f / initialSizes[index1])
        val alpha1 = ObjectAnimator.ofFloat(imageView1, View.ALPHA, initialAlphas[index1], 0f)

        // 第二个 ImageView 动画：向左移动 24dp，放大到 56dp，透明度从对应初始状态到 1
        val translate2 = ObjectAnimator.ofFloat(imageView2, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX2 = ObjectAnimator.ofFloat(imageView2, View.SCALE_X, 1f, 56f / initialSizes[index2])
        val scaleY2 = ObjectAnimator.ofFloat(imageView2, View.SCALE_Y, 1f, 56f / initialSizes[index2])
        val alpha2 = ObjectAnimator.ofFloat(imageView2, View.ALPHA, initialAlphas[index2], 1f)

        // 第三个 ImageView 动画：向左移动 24dp，放大到 40dp，透明度从对应初始状态到 0.5
        val translate3 = ObjectAnimator.ofFloat(imageView3, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX3 = ObjectAnimator.ofFloat(imageView3, View.SCALE_X, 1f, 40f / initialSizes[index3])
        val scaleY3 = ObjectAnimator.ofFloat(imageView3, View.SCALE_Y, 1f, 40f / initialSizes[index3])
        val alpha3 = ObjectAnimator.ofFloat(imageView3, View.ALPHA, initialAlphas[index3], 0.5f)

        // 创建并启动动画集
        val animatorSet1 = AnimatorSet()
        animatorSet1.playTogether(translate1, scaleX1, scaleY1, alpha1)
        animatorSet1.duration = 1000

        val animatorSet2 = AnimatorSet()
        animatorSet2.playTogether(translate2, scaleX2, scaleY2, alpha2)
        animatorSet2.duration = 1000

        val animatorSet3 = AnimatorSet()
        animatorSet3.playTogether(translate3, scaleX3, scaleY3, alpha3)
        animatorSet3.duration = 1000

        // 启动所有动画
        animatorSet1.start()
        animatorSet2.start()
        animatorSet3.start()

        // 监听动画结束，准备下一轮循环
        animatorSet3.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                // 动画结束后，更新 ImageView 的状态
                resetAndLoop()
            }
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }

    private fun resetAndLoop() {
        // 重置所有 ImageView 的位置
        imageView1.translationX = 0f
        imageView2.translationX = 0f
        imageView3.translationX = 0f

        // 增加动画执行次数
        animationCount++

        // 根据动画执行次数更新当前索引，实现循环
        currentIndices = when (animationCount % 3) {
            1 -> listOf(
                2, // 第一次动画后：第一个 ImageView 变为第三个的初始状态
                0, // 第二个 ImageView 变为第一个的初始状态
                1  // 第三个 ImageView 变为第二个的初始状态
            )
            2 -> listOf(
                1, // 第二次动画后：第一个 ImageView 变为第二个的初始状态
                2, // 第二个 ImageView 变为第三个的初始状态
                0  // 第三个 ImageView 变为第一个的初始状态
            )
            else -> listOf(
                0, // 第三次动画后：回到初始状态
                1,
                2
            )
        }

        // 更新 ImageView 的位置、尺寸和透明度
        updateImageViewStates()

        // 延迟一下开始下一轮动画，确保布局更新完成
        imageView1.postDelayed({
            startAnimations()
        }, 100)
    }

    private fun updateImageViewStates() {
        // 获取当前每个 ImageView 对应的初始状态索引
        val index1 = currentIndices[0]
        val index2 = currentIndices[1]
        val index3 = currentIndices[2]

        // 更新第一个 ImageView
        val layoutParams1 = imageView1.layoutParams as android.widget.FrameLayout.LayoutParams
        layoutParams1.width = initialSizes[index1].dpToPx().toInt()
        layoutParams1.height = initialSizes[index1].dpToPx().toInt()
        layoutParams1.leftMargin = initialMargins[index1].dpToPx().toInt()
        imageView1.layoutParams = layoutParams1
        imageView1.alpha = initialAlphas[index1]

        // 更新第二个 ImageView
        val layoutParams2 = imageView2.layoutParams as android.widget.FrameLayout.LayoutParams
        layoutParams2.width = initialSizes[index2].dpToPx().toInt()
        layoutParams2.height = initialSizes[index2].dpToPx().toInt()
        layoutParams2.leftMargin = initialMargins[index2].dpToPx().toInt()
        imageView2.layoutParams = layoutParams2
        imageView2.alpha = initialAlphas[index2]

        // 更新第三个 ImageView
        val layoutParams3 = imageView3.layoutParams as android.widget.FrameLayout.LayoutParams
        layoutParams3.width = initialSizes[index3].dpToPx().toInt()
        layoutParams3.height = initialSizes[index3].dpToPx().toInt()
        layoutParams3.leftMargin = initialMargins[index3].dpToPx().toInt()
        imageView3.layoutParams = layoutParams3
        imageView3.alpha = initialAlphas[index3]

        // 应用布局更改
        imageView1.requestLayout()
        imageView2.requestLayout()
        imageView3.requestLayout()
    }

    // 扩展函数：将 dp 转换为 px
    private fun Number.dpToPx(): Float {
        return this.toFloat() * resources.displayMetrics.density
    }
}
