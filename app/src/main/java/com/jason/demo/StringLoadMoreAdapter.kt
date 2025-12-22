package com.jason.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * 基于 LoadMoreAdapter 的示例实现：同一列表中混排普通与高亮两类文案。
 */
class StringLoadMoreAdapter : LoadMoreAdapter<String>() {

    override fun getDataViewType(position: Int, item: String): Int {
        // 每 5 条显示一个高亮样式，示例多类型场景
        return if ((position + 1) % 5 == 0) VIEW_TYPE_HIGHLIGHT else VIEW_TYPE_NORMAL
    }

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<String> {
        val layoutId = if (viewType == VIEW_TYPE_HIGHLIGHT) {
            R.layout.item_load_more_highlight
        } else {
            R.layout.item_load_more_content
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return StringItemViewHolder(view)
    }

    private class StringItemViewHolder(itemView: View) : BaseItemViewHolder<String>(itemView) {
        private val content: TextView = itemView.findViewById(R.id.item_text)

        override fun bind(item: String, position: Int) {
            content.text = item
        }
    }

    companion object {
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_HIGHLIGHT = 2
    }
}

