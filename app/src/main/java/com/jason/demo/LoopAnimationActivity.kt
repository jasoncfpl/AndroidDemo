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
        // 第一个 ImageView 动画：向左移动 24dp，放大到 64dp，透明度从 1 到 0
        val translate1 = ObjectAnimator.ofFloat(imageView1, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX1 = ObjectAnimator.ofFloat(imageView1, View.SCALE_X, 1f, 64f / 56f)
        val scaleY1 = ObjectAnimator.ofFloat(imageView1, View.SCALE_Y, 1f, 64f / 56f)
        val alpha1 = ObjectAnimator.ofFloat(imageView1, View.ALPHA, 1f, 0f)

        // 第二个 ImageView 动画：向左移动 24dp，放大到 56dp，透明度从 0.5 到 1
        val translate2 = ObjectAnimator.ofFloat(imageView2, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX2 = ObjectAnimator.ofFloat(imageView2, View.SCALE_X, 1f, 56f / 40f)
        val scaleY2 = ObjectAnimator.ofFloat(imageView2, View.SCALE_Y, 1f, 56f / 40f)
        val alpha2 = ObjectAnimator.ofFloat(imageView2, View.ALPHA, 0.5f, 1f)

        // 第三个 ImageView 动画：向左移动 24dp，放大到 40dp，透明度从 0.2 到 0.5
        val translate3 = ObjectAnimator.ofFloat(imageView3, View.TRANSLATION_X, 0f, -24.dpToPx())
        val scaleX3 = ObjectAnimator.ofFloat(imageView3, View.SCALE_X, 1f, 40f / 32f)
        val scaleY3 = ObjectAnimator.ofFloat(imageView3, View.SCALE_Y, 1f, 40f / 32f)
        val alpha3 = ObjectAnimator.ofFloat(imageView3, View.ALPHA, 0.2f, 0.5f)

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
    }

    // 扩展函数：将 dp 转换为 px
    private fun Number.dpToPx(): Float {
        return this.toFloat() * resources.displayMetrics.density
    }
}
