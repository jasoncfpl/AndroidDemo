package com.jason.demo

import android.annotation.SuppressLint
import android.content.Intent
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


        findViewById<Button>(R.id.dialog_btn).setOnClickListener {
            startActivity(Intent(this@MainActivity, DialogsActivity::class.java))

        }



        findViewById<Button>(R.id.btn_view_pager).setOnClickListener {
            val index = 1
//            startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
            test()
//            test2()
        }

    }
    private val TAG = "MainActivity"

    private fun test2() {
        Observable.create<String> {	// it == CreateEmitter
            Log.i(TAG, "事件产生线程：${Thread.currentThread().name}")
            it.onNext("rx")
            it.onCompleted()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext({
                Log.i(TAG, "事件消费线程 doOnNext：${Thread.currentThread().name}")
            })
            .observeOn(Schedulers.io())
            .subscribe {	// onNext
                Log.i(TAG, "事件消费线程：${Thread.currentThread().name}")
                Log.i(TAG, it)
            }

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