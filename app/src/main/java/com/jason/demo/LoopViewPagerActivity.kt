package com.jason.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jason.demo.adapter.LoopViewPagerAdapter
import kotlinx.android.synthetic.main.activity_loop_view_pager.*

class LoopViewPagerActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: LoopViewPagerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = Runnable {
        val currentItem = viewPager2.currentItem
        viewPager2.setCurrentItem(currentItem + 1, true)
        handler.postDelayed(autoScrollRunnable, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_view_pager)

        viewPager2 = findViewById(R.id.view_pager2)
        
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
