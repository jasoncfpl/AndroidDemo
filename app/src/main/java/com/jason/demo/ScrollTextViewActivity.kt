package com.jason.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ScrollTextViewActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_text_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ScrollingTextView>(R.id.scrolling_text_view).setScrollDuration(4000)
    }
}

