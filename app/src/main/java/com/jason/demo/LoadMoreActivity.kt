package com.jason.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StringLoadMoreAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val pageSize = 20
    private var isLoading = false
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_more)

        recyclerView = findViewById(R.id.recycler_load_more)
        adapter = StringLoadMoreAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadMoreData()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy <= 0) return

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val totalCount = adapter.itemCount

                if (!isLoading && hasMore && lastVisible >= totalCount - 3) {
                    loadMoreData()
                }
            }
        })
    }

    private fun loadMoreData() {
        if (isLoading || !hasMore) return
        isLoading = true
        adapter.setLoading(true)

        handler.postDelayed({
            val newItems = generatePage()
            if (newItems.isEmpty()) {
                hasMore = false
                adapter.setHasMore(false)
            } else {
                adapter.appendItems(newItems)
            }
            isLoading = false
            adapter.setLoading(false)
        }, 1200L)
    }

    private fun generatePage(): List<String> {
        if (currentPage >= 3) { // 模拟最多 3 页数据
            return emptyList()
        }
        val start = currentPage * pageSize
        currentPage++
        return List(pageSize) { index -> "示例数据 #${start + index + 1}" }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}

