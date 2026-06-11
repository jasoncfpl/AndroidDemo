package com.jason.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

//t1
class AppLayoutActivity : AppCompatActivity() {
    // recyclerView中的item数据源
    private val itemData  = mutableListOf<String>()
    // tabLayout的标题
    private val tabTitle = arrayOf("标题1", "标题2", "标题3", "标题4", "标题5", "标题6", "标题7", "标题8", "标题9")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_layout)
        initItemData()

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        // 设置横向滑动
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE;

        val viewPager2: ViewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(MyViewPager2Adapter(itemData, tabTitle, this))
        val tabLayoutMediator: TabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position -> // 设置tabLayout的标题
            tab.setText(tabTitle[position]);
        };
        // 应用生效
        tabLayoutMediator.attach()
    }


    /**
     * 初始化recyclerView中的item数据源
     */
    private fun initItemData() {

        for (i in 0..100) {
            itemData.add("$i")
        }

    }


}