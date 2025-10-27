package com.jason.demo.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import com.jason.demo.R

/**
 *   @author li jia
 *   @date 2025/10/27 16:57
 *   @description:
 */
class RightSideDialog(context: Context) : Dialog(context, R.style.DialogRight) {

    private var mRootView: ViewGroup

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_right_side)
        setCancelable(true)

        val attr = window?.attributes
        attr?.gravity = Gravity.END
        attr?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        attr?.height = ViewGroup.LayoutParams.MATCH_PARENT
        window?.attributes = attr

        mRootView = findViewById(R.id.main)

    }


    override fun show() {
        super.show()
        mRootView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_right_in))
    }

    override fun dismiss() {
        mRootView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.dialog_right_out))
        super.dismiss()
    }
}