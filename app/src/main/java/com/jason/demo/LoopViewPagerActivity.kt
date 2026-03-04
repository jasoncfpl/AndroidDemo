package com.jason.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jason.demo.adapter.LoopViewPagerAdapter
class LoopViewPagerActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: LoopViewPagerAdapter
    private lateinit var indicator: com.jason.demo.widget.ViewPagerIndicator
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable: Runnable = Runnable {
        val currentItem = viewPager2.currentItem
        viewPager2.setCurrentItem(currentItem + 1, true)
        handler.postDelayed(autoScrollRunnable, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_view_pager)

        viewPager2 = findViewById(R.id.view_pager2)
        indicator = findViewById(R.id.indicator)
        
//        // 修改ViewPager2的切换动画时长
//        // 使用自定义PageTransformer来控制动画效果
//        viewPager2.setPageTransformer(object : ViewPager2.PageTransformer {
//            override fun transformPage(page: android.view.View, position: Float) {
//                // 实现自定义的页面变换效果，通过控制透明度和缩放来影响视觉速度
//                page.alpha = 1 - Math.abs(position)
//                page.scaleX = 1 - Math.abs(position) * 0.1f
//                page.scaleY = 1 - Math.abs(position) * 0.1f
//
//                // 控制页面的位置，创造更慢的动画效果
//                page.translationX = page.width * -position * 0.4f // 调整这个值可以改变滑动速度
//            }
//        })
        
        // 模拟数据
        val images = listOf(
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground
        )

        adapter = LoopViewPagerAdapter(images)
        viewPager2.adapter = adapter
        
        // 设置初始位置为中间，实现无限轮播效果
        viewPager2.setCurrentItem(adapter.itemCount / 2, false)
        
        // 设置指示器
        indicator.setViewPager(viewPager2, images.size)
        
        // 自动轮播
        handler.postDelayed(autoScrollRunnable, 3000)
        
        // 添加触摸事件监听，控制自动轮播
        viewPager2.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // 用户开始触摸，取消自动轮播
                    handler.removeCallbacks(autoScrollRunnable)
                }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    // 用户结束触摸，重新开始自动轮播
                    handler.postDelayed(autoScrollRunnable, 3000)
                }
            }
            false
        }
        
        // 监听页面变化，更新指示器
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 计算实际位置
                val realPosition = position % images.size
                indicator.setCurrentItem(realPosition)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止自动轮播
        handler.removeCallbacks(autoScrollRunnable)
    }
}
