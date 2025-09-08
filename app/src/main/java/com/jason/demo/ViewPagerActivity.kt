package com.jason.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jason.demo.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPagerBinding
    private lateinit var galleryAdapter: GalleryAdapter
    
    // 示例图片资源列表
    private val imageList = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_background
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        // 初始化适配器
        galleryAdapter = GalleryAdapter(imageList)
        
        // 设置RecyclerView
        binding.recyclerViewGallery.apply {
            // 使用自定义的LinearLayoutManager实现居中效果
            val layoutManager = object : LinearLayoutManager(this@ViewPagerActivity, HORIZONTAL, false) {
                override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
                    // 为了预加载，增加额外的布局空间
                    extraLayoutSpace[0] = width / 2
                    extraLayoutSpace[1] = width / 2
                }
            }
            
            this.layoutManager = layoutManager
            adapter = galleryAdapter
            
            // 使用PagerSnapHelper实现分页效果
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            
            // 添加滚动监听器实现缩放效果
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    applyScaleEffect(recyclerView)
                }
            })
            
            // 初始应用缩放效果
            post { applyScaleEffect(this) }
        }
    }
    
    // 应用缩放效果
    private fun applyScaleEffect(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val centerOfScreen = recyclerView.width / 2
        
        // 遍历可见的item
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenter = (child.left + child.right) / 2
            val distanceFromCenter = Math.abs(childCenter - centerOfScreen)
            
            // 计算缩放比例，中心位置为0.8倍，远离中心的更小
            val scale = Math.max(0.8f, 1f - 0.2f * (distanceFromCenter.toFloat() / recyclerView.width))
            
            // 应用缩放
            child.scaleX = scale
            child.scaleY = scale
        }
    }
}