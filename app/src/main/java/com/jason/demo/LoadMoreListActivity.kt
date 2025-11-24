package com.jason.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 带有加载更多功能的列表页面
 */
class LoadMoreListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LoadMoreAdapter<String>
    private val handler = Handler(Looper.getMainLooper())

    private var currentPage = 1
    private val pageSize = 15
    private val maxPage = 4
    private var isLoading = false
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_more_list)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = LoadMoreAdapter(
            itemLayoutId = R.layout.item_load_more_content
        ) { itemView, data, _ ->
            val textView = itemView.findViewById<TextView>(R.id.item_text)
            textView.text = data
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy <= 0 || isLoading || !hasMore) {
                    return
                }
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2) {
                    loadMore()
                }
            }
        })

        loadInitialData()
    }

    private fun loadInitialData() {
        val initialData = generatePageData(currentPage)
        adapter.setItems(initialData)
        hasMore = currentPage < maxPage
        adapter.setHasMore(hasMore)
    }

    private fun loadMore() {
        if (isLoading || !hasMore) {
            return
        }
        isLoading = true
        adapter.setLoading(true)

        handler.postDelayed({
            currentPage++
            val newData = generatePageData(currentPage)
            adapter.appendItems(newData)
            hasMore = currentPage < maxPage
            adapter.setHasMore(hasMore)
            adapter.setLoading(false)
            isLoading = false
        }, 1200)
    }

    private fun generatePageData(page: Int): List<String> {
        val start = (page - 1) * pageSize + 1
        val end = start + pageSize - 1
        return (start..end).map { "第 $it 条数据" }
    }
}

