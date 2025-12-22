package com.jason.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * “加载更多”基础适配器。通过继承该类，可在不同数据类型/布局上复用加载更多逻辑。
 */
abstract class LoadMoreAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = mutableListOf<T>()
    private var isLoading = false
    private var hasMore = true

    override fun getItemCount(): Int = items.size + 1 // footer

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) {
            getDataViewType(position, items[position])
        } else {
            VIEW_TYPE_FOOTER
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            val footerView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_more_footer, parent, false)
            FooterViewHolder(footerView)
        } else {
            createItemViewHolder(parent, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseItemViewHolder<*> && position < items.size) {
            @Suppress("UNCHECKED_CAST")
            (holder as BaseItemViewHolder<T>).bind(items[position], position)
        } else if (holder is FooterViewHolder) {
            holder.bind(isLoading, hasMore)
        }
    }

    /**
     * 子类可以通过重写该方法，为不同数据返回不同 viewType。
     */
    protected open fun getDataViewType(position: Int, item: T): Int = DEFAULT_ITEM_VIEW_TYPE

    /**
     * 子类需要实现，创建指定 viewType 的 Item ViewHolder。
     */
    protected abstract fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<T>

    /**
     * 基础 Item ViewHolder，子类可以继承它来绑定自己的数据。
     */
    abstract class BaseItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
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
        protected const val DEFAULT_ITEM_VIEW_TYPE = 0
        private const val VIEW_TYPE_FOOTER = Int.MAX_VALUE
    }
}

