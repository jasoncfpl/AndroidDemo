package com.jason.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jason.demo.R

class LoopViewPagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<LoopViewPagerAdapter.ViewHolder>() {

    // 首尾各添加一个元素，用于实现循环滚动
    override fun getItemCount(): Int {
        return if (images.size > 1) images.size + 2 else images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loop_view_pager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realPosition = when (position) {
            0 -> images.size - 1 // 第一个位置显示最后一个元素
            itemCount - 1 -> 0 // 最后一个位置显示第一个元素
            else -> position - 1 // 中间位置正常显示
        }
        holder.imageView.setImageResource(images[realPosition])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}
