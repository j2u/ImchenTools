package com.imchen.floatview.view

import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
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

class FloatBallView(context: Context?) : LinearLayout(context) {

    var startX: Float = 0.0F
    var startY: Float = 0.0F
    var endX: Float = 0.0F
    var endY: Float = 0.0F
    var tmpX: Float = 0.0F
    var tmpY: Float = 0.0F
    var icon: ImageView? = null
    var mFloatViewManager:WindowManager? =null
    var mLayoutParam: WindowManager.LayoutParams? = null
    fun initView() {
        icon = ImageView(context)
        icon!!.setImageResource(R.mipmap.ic_launcher_round)
        this.addView(icon)
        icon!!.setOnTouchListener(floatBallListener())
    }

    fun createLayoutParam(): WindowManager.LayoutParams {
        mLayoutParam= WindowManager.LayoutParams()
        mLayoutParam!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParam!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParam!!.gravity = Gravity.TOP or Gravity.START
        mLayoutParam!!.x = 0
        mLayoutParam!!.y = 1000
        mLayoutParam!!.format = PixelFormat.RGBA_8888
        mLayoutParam!!.type = WindowManager.LayoutParams.TYPE_PHONE
        mLayoutParam!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        return mLayoutParam as WindowManager.LayoutParams
    }


    internal inner class floatBallListener : OnTouchListener {
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX=p1.rawX
                    startY=p1.rawY
                    Log.d("FloatBallView", "ACTION_DOWN: rawX->" + p1.rawX + " rawY->" + p1.rawY)
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("FloatBallView", "ACTION_MOVE: rawX->" + p1.rawX + " rawY->" + p1.rawY)
                    tmpX=p1.rawX
                    tmpY=p1.rawY
                    mLayoutParam!!.x= tmpX.toInt()
                    mLayoutParam!!.y= tmpY.toInt()
                    mFloatViewManager?.updateViewLayout(this@FloatBallView,mLayoutParam)
                    Log.d("FloatBallView","X:"+mLayoutParam!!.x+" Y:"+mLayoutParam!!.y)
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("FloatBallView", "ACTION_UP: rawX->" + p1.rawX + " rawY->" + p1.rawY)
                    endX=p1.rawX
                    endY=p1.rawY
                    mLayoutParam!!.x= endX.toInt()
                    mLayoutParam!!.y= endY.toInt()
                    mFloatViewManager?.updateViewLayout(this@FloatBallView,mLayoutParam)
                }
            }
            return true
        }

    }

}