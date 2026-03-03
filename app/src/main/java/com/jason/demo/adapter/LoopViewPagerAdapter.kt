package com.jason.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jason.demo.R

class LoopViewPagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<LoopViewPagerAdapter.ViewHolder>() {

    // 实现无限轮播的关键：设置一个很大的数值作为 itemCount
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loop_view_pager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 计算实际位置
        val realPosition = position % images.size
        holder.imageView.setImageResource(images[realPosition])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}
