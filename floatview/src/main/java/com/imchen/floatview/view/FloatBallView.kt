package com.imchen.floatview.view

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.imchen.floatview.R

/**
 * Created by imchen on 2017/12/5.
 */

class FloatBallView(context: Context?) : LinearLayout(context){
    var icon: ImageView?=null

    fun initView(){
        icon= ImageView(context)
        icon!!.setImageResource(R.mipmap.ic_launcher_round)
        this.addView(icon)
    }
    fun createLayoutParam(): WindowManager.LayoutParams{
        var layoutParam=WindowManager.LayoutParams()
        layoutParam.height=WindowManager.LayoutParams.WRAP_CONTENT
        layoutParam.width=WindowManager.LayoutParams.WRAP_CONTENT
        layoutParam.gravity=Gravity.CENTER
        layoutParam.format=PixelFormat.RGBA_8888
        layoutParam.type=WindowManager.LayoutParams.TYPE_TOAST
        layoutParam.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        return layoutParam
    }

    class floatBallListener: OnTouchListener{
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}