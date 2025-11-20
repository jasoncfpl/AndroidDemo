package com.jason.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jason.demo.dialog.DialogsActivity
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var textView:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        textView = findViewById<TextView>(R.id.app_bar_layout)

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


        findViewById<Button>(R.id.dialog_btn).setOnClickListener {
            startActivity(Intent(this@MainActivity, DialogsActivity::class.java))

        }



        findViewById<Button>(R.id.btn_view_pager).setOnClickListener {
            val index = 1
//            startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
//            test()
            test1()
//            test2()
        }

        findViewById<Button>(R.id.btn_scroll_text_view).setOnClickListener {
            startActivity(Intent(this@MainActivity, ScrollTextViewActivity::class.java))
        }
    }
    private val TAG = "MainActivity"

    private fun test1() {
        val colors = intArrayOf(Color.WHITE, Color.parseColor("#BEFF45")) //颜色的数组
        val position = floatArrayOf(0.8f, 1.0f) //颜色渐变位置的数组
        Log.i(TAG, "test1: ${textView.paint.textSize * textView.text.length}")

        //        LinearGradient mLinearGradient = new LinearGradient(getMeasuredWidth()/2,0,0,getMeasuredHeight()/2,Color.RED,Color.GREEN, Shader.TileMode.CLAMP);
        val mLinearGradient = LinearGradient(
            textView.paint.textSize * textView.text.length / 3f ,
            0f,
            textView.paint.textSize * textView.text.length - 600f,
           textView.paint.textSize,
            colors,
            position,
            Shader.TileMode.CLAMP
        )
        textView.paint.setShader(mLinearGradient)
        textView.invalidate()
    }

    private fun test2() {
        Observable.just("1")
            .observeOn(Schedulers.computation())
            .map {
                return@map listOf<String>("4","2","1","3")
            }

            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { chatContacts ->
                Log.i(TAG, "test2:${Thread.currentThread().name} " )
                //先排序
                if (chatContacts != null) {
                    //本地删除的不显示
                    val iterator = chatContacts.iterator()
                    while (iterator.hasNext()) {
                        val entity = iterator.next()

                    }
                }
            }.map {
                return@map it
            }
            .flatMap({
                Log.i(TAG, "test2 flatMap:${Thread.currentThread().name} " )
                return@flatMap Observable.just(it)
            }) //数据库的数据和网络的数据合并
            .observeOn(Schedulers.computation())
            .map { net ->
                Log.i(TAG, "test2 map:${Thread.currentThread().name} " )
                return@map null
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { contactData ->
                Log.i(TAG, "test2 doOnNext1:${Thread.currentThread().name} " )
            }
            .observeOn(Schedulers.computation())
            .doOnNext {
                Log.i(TAG, "test2 doOnNext2:${Thread.currentThread().name} " )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                Log.i(TAG, "subscribe:${Thread.currentThread().name} ")
            })

    }
    private fun test() {
        Log.i(TAG, "test:${Thread.currentThread().name} ")
        Observable.just("") {
            Log.i(TAG, "just: ${Thread.currentThread().name}")
        }
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.computation())
            .map {
                Log.i(TAG, "map1: ${Thread.currentThread().name}")
                val list = mutableListOf<String>()
                for (i in 0..20) {
                    list.add("item-$i")
                }
                return@map list
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { chatContacts ->
                Log.i(TAG, "doOnNext chatContacts: ${Thread.currentThread().name}")
            }
            .map {
                Log.i(TAG, "map2: ${Thread.currentThread().name}")
                return@map null
            }
            .observeOn(Schedulers.computation())
            .map { net ->
                Log.i(TAG, "map3: ${Thread.currentThread().name}")
                return@map null
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { contactData ->
                Log.i(TAG, "doOnNext contactData: ${Thread.currentThread().name}")
                if (contactData == null) {
                    return@doOnNext
                }
            }
            .observeOn(Schedulers.computation())
            .doOnNext {
                Log.i(TAG, "doOnNext final: ${Thread.currentThread().name}")
            }

//            .observeOn(AndroidSchedulers.mainThread())
//            .observeOn(Schedulers.io())
//            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.i(TAG, "subscribe : ${Thread.currentThread().name}")
            })

        Log.i(TAG, "test2:${Thread.currentThread().name} ")
    }
}