package com.jason.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.app_bar_layout).setOnClickListener {
            startActivity(Intent(this@MainActivity, AppLayoutActivity::class.java))
        }

        findViewById<TextView>(R.id.move_iew).setOnClickListener {
            startActivity(Intent(this@MainActivity, MoveViewActivity::class.java))
        }

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_view_pager).setOnClickListener {
            startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
        }


    }
}