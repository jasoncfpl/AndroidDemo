package com.jason.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoadMoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<String>()
    private var isLoading = false
    private var hasMore = true

    override fun getItemCount(): Int = items.size + 1 // footer

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(R.layout.item_load_more_content, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_load_more_footer, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder && position < items.size) {
            holder.bind(items[position])
        } else if (holder is FooterViewHolder) {
            holder.bind(isLoading, hasMore)
        }
    }

    fun setItems(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun appendItems(newItems: List<String>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }

    fun setLoading(loading: Boolean) {
        if (isLoading == loading) return
        isLoading = loading
        notifyItemChanged(items.size)
    }

    fun setHasMore(hasMore: Boolean) {
        if (this.hasMore == hasMore) return
        this.hasMore = hasMore
        notifyItemChanged(items.size)
    }

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.item_text)

        fun bind(text: String) {
            textView.text = text
        }
    }

    private class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progress: ProgressBar = itemView.findViewById(R.id.footer_progress)
        private val message: TextView = itemView.findViewById(R.id.footer_message)

        fun bind(isLoading: Boolean, hasMore: Boolean) {
            when {
                isLoading -> {
                    progress.visibility = View.VISIBLE
                    message.text = itemView.context.getString(R.string.load_more_loading)
                }
                !hasMore -> {
                    progress.visibility = View.GONE
                    message.text = itemView.context.getString(R.string.load_more_no_more)
                }
                else -> {
                    progress.visibility = View.GONE
                    message.text = itemView.context.getString(R.string.load_more_pull_up)
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_FOOTER = 1
    }
}

