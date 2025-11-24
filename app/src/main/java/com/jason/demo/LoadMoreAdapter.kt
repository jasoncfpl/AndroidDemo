package com.jason.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 通用加载更多适配器，支持任意数据类型。
 *
 * @param T 数据类型
 * @param itemLayoutId 普通 item 布局
 * @param bindItem 绑定回调，携带 itemView、数据和 position
 */
class LoadMoreAdapter<T>(@LayoutRes private val itemLayoutId: Int, private val bindItem: (itemView: View, itemData: T, position: Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<T>()
    private var isLoading = false
    private var hasMore = true

    override fun getItemCount(): Int = items.size + 1 // footer

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) VIEW_TYPE_ITEM else VIEW_TYPE_FOOTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(itemLayoutId, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_load_more_footer, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder && position < items.size) {
            val item = items[position]
            bindItem(holder.itemView, item, position)
        } else if (holder is FooterViewHolder) {
            holder.bind(isLoading, hasMore)
        }
    }

    fun setItems(newItems: Collection<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun appendItems(newItems: Collection<T>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
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

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

