package com.imchen.floatview

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.lang.reflect.Method

/**
 * Created by imchen on 2017/12/4.
 */

class FloatViewManager private constructor() {
    //当某个变量的值可以为 null 的时候，必须在声明处的类型后添加 ? 来标识该引用可为空。
    //val 一次赋值，只读变量
    //var 可变变量
//    val floatViewManager = FloatViewManager
    private var showingViewSum = 0
    private var mContext: Context? = null
    private var windowsManger: WindowManager? = null

    init {
        val cls = Class.forName("android.app.ActivityThread")
        val method: Method? = cls.getDeclaredMethod("currentActivityThread")
        val curObj = method!!.invoke(cls)
        val methodMain = curObj.javaClass.getDeclaredMethod("getApplication")
        val context: Context = methodMain.invoke(curObj) as Context
        this.mContext = context
    }

    companion object {
        fun getInstance(): FloatViewManager {
            return Inner.floatViewManager
        }
    }

    private object Inner {
        val floatViewManager = FloatViewManager()
    }


    fun showView(view: View, layoutParam: WindowManager.LayoutParams) {
        showingViewSum++
        val showViewStr = "showing view count:$showingViewSum"
        println(showViewStr)
        addCoustomView(view, layoutParam)
    }

    fun hideView(view: View) {
        if (view != null) {
            windowsManger!!.removeView(view)
        }else{
            return
        }
        showingViewSum--
        val hideViewStr = "hide view now! still show view count:$showingViewSum"
        println(hideViewStr)
        toast("remove a view from screen!")
    }

    private fun addCoustomView(view: View, layoutParam: WindowManager.LayoutParams) {
        windowsManger = mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (!view.isShown) {
            windowsManger!!.addView(view, layoutParam)
            toast("add a view to screen!")
        }

    }

    fun reflectGetContext(): Context? {
        val cls = Class.forName("android.app.ActivityThread")
        val method: Method? = cls.getDeclaredMethod("currentActivityThread")
        val curObj = method!!.invoke(cls)
        val methodMain = curObj.javaClass.getDeclaredMethod("getApplication")
        val context: Context = methodMain.invoke(curObj) as Context
        this.mContext = context
        return mContext
    }

    private fun toast(msg: String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
    }
}