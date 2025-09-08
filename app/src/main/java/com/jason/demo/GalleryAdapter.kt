package com.jason.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jason.demo.databinding.ItemGalleryBinding

class GalleryAdapter(private val imageList: List<Int>) : 
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageResId = imageList[position]
        holder.bind(imageResId)
    }
    
    override fun getItemCount(): Int = imageList.size
    
    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(imageResId: Int) {
            binding.imageViewGallery.setImageResource(imageResId)
        }
    }
}