package com.jason.demo

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.lang.ref.WeakReference

/**
 * iPhone 风格的 Toast 工具类
 * 
 * @author li jia
 * @date 2025/01/XX
 * @description: 提供类似 iOS 风格的 Toast 提示，包括圆角背景、淡入淡出动画等
 */
object ToastUtils {

    private const val DEFAULT_DURATION = 2000L // 默认显示时长（毫秒）
    private const val ANIMATION_DURATION = 300L // 动画时长（毫秒）
    
    private var currentToastViewRef: WeakReference<View>? = null
    private var currentHandler: Handler? = null
    private var currentRunnable: Runnable? = null

    /**
     * 显示 Toast
     * 
     * @param context 上下文
     * @param message 提示信息
     * @param duration 显示时长（毫秒），默认 2000ms
     * @param gravity 位置，默认居中
     * @param iconRes 图标资源 ID，可选
     */
    @JvmOverloads
    fun show(
        context: Context,
        message: String,
        duration: Long = DEFAULT_DURATION,
        gravity: Int = Gravity.CENTER,
        @DrawableRes iconRes: Int = 0
    ) {
        // 如果已有 Toast 正在显示，先隐藏它
        hide()

        val activity = getActivity(context) ?: return
        val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            ?: return

        // 创建 Toast 视图
        val toastView = LayoutInflater.from(context).inflate(R.layout.toast_iphone, null)
        val textView = toastView.findViewById<TextView>(R.id.toast_text)
        val iconView = toastView.findViewById<ImageView>(R.id.toast_icon)
        
        textView.text = message
        
        // 设置图标
        if (iconRes != 0) {
            iconView.setImageResource(iconRes)
            iconView.visibility = View.VISIBLE
            // 如果有图标，TextView 的 marginTop 已经在布局中设置了
        } else {
            iconView.visibility = View.GONE
            // 如果没有图标，移除 TextView 的 marginTop
            val layoutParams = textView.layoutParams as android.view.ViewGroup.MarginLayoutParams
            layoutParams.topMargin = 0
            textView.layoutParams = layoutParams
        }

        // 设置布局参数
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            when (gravity) {
                Gravity.CENTER -> {
                    this.gravity = Gravity.CENTER
                }
                Gravity.BOTTOM -> {
                    this.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    bottomMargin = dpToPx(context, 100)
                }
                Gravity.TOP -> {
                    this.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    topMargin = dpToPx(context, 100)
                }
                else -> {
                    this.gravity = Gravity.CENTER
                }
            }
        }

        // 添加到根视图
        rootView.addView(toastView, layoutParams)

        // 保存当前 Toast 视图（使用弱引用避免内存泄漏）
        currentToastViewRef = WeakReference(toastView)

        // 淡入动画
        val fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.toast_fade_in)
        toastView.startAnimation(fadeInAnim)
        toastView.visibility = View.VISIBLE

        // 设置自动隐藏
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            hide()
        }
        handler.postDelayed(runnable, duration)
        
        currentHandler = handler
        currentRunnable = runnable
    }

    /**
     * 显示 Toast（使用字符串资源）
     */
    @JvmOverloads
    fun show(
        context: Context,
        @StringRes messageRes: Int,
        duration: Long = DEFAULT_DURATION,
        gravity: Int = Gravity.CENTER,
        @DrawableRes iconRes: Int = 0
    ) {
        show(context, context.getString(messageRes), duration, gravity, iconRes)
    }

    /**
     * 显示短时 Toast（1.5秒）
     */
    fun showShort(context: Context, message: String) {
        show(context, message, 1500L)
    }

    /**
     * 显示短时 Toast（使用字符串资源）
     */
    fun showShort(context: Context, @StringRes messageRes: Int) {
        show(context, messageRes, 1500L)
    }

    /**
     * 显示长时 Toast（3秒）
     */
    fun showLong(context: Context, message: String) {
        show(context, message, 3000L)
    }

    /**
     * 显示长时 Toast（使用字符串资源）
     */
    fun showLong(context: Context, @StringRes messageRes: Int) {
        show(context, messageRes, 3000L)
    }

    /**
     * 显示底部 Toast
     */
    @JvmOverloads
    fun showBottom(context: Context, message: String, duration: Long = DEFAULT_DURATION) {
        show(context, message, duration, Gravity.BOTTOM)
    }

    /**
     * 显示底部 Toast（使用字符串资源）
     */
    @JvmOverloads
    fun showBottom(context: Context, @StringRes messageRes: Int, duration: Long = DEFAULT_DURATION) {
        show(context, messageRes, duration, Gravity.BOTTOM)
    }

    /**
     * 隐藏当前显示的 Toast
     */
    fun hide() {
        currentHandler?.removeCallbacks(currentRunnable ?: return)
        currentHandler = null
        currentRunnable = null

        val toastView = currentToastViewRef?.get() ?: return
        val context = toastView.context

        // 淡出动画
        val fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.toast_fade_out)
        fadeOutAnim.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // 动画结束后移除视图
                (toastView.parent as? ViewGroup)?.removeView(toastView)
            }
        })
        toastView.startAnimation(fadeOutAnim)

        currentToastViewRef = null
    }

    /**
     * 获取 Activity 上下文
     */
    private fun getActivity(context: Context): Activity? {
        var ctx = context
        while (ctx is android.content.ContextWrapper) {
            if (ctx is Activity) {
                return ctx
            }
            ctx = ctx.baseContext
        }
        return null
    }

    /**
     * dp 转 px
     */
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }
}

