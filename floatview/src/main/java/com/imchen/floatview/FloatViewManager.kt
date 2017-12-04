package com.imchen.floatview

import android.view.View

/**
 * Created by imchen on 2017/12/4.
 */

class FloatViewManager {
    //当某个变量的值可以为 null 的时候，必须在声明处的类型后添加 ? 来标识该引用可为空。
    //val 一次赋值，只读变量
    //var 可变变量
    var floatManager = null
    var showingView = 0

    fun getInstance(): FloatViewManager {
        if (floatManager == null) {
            floatManager=FloatViewManager()
        }
        return floatManager
    }

    class FloatViewManager()


    fun showView(view: View) {
        showingView++
        val showViewStr="showing view count:$showingView"
        println(showViewStr)
    }

    fun hideView() {
        showingView--
        val hideViewStr="hide view now! still show view count:$showingView"
        println(hideViewStr)
    }
//
//    fun countShowingView(): Int {
//
//    }
}