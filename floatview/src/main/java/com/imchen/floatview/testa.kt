package com.imchen.floatview

import android.content.Context

import java.lang.reflect.Method

/**
 * Created by imchen on 2017/12/5.
 */

class testa {

    var mContext: Context?=null
    //获取currentActivityThread 对象
    //获取 Context对象
//        get() {
//            val context: Context? = try {
//                val ActivityThread = Class.forName("android.app.ActivityThread")
//
//                var method: Method? = null
//                method = ActivityThread.getMethod("currentActivityThread")
//                val currentActivityThread = method!!.invoke(ActivityThread)
//
//                val method2 = currentActivityThread.javaClass.getMethod("getApplication")
//                mContext = method2.invoke(currentActivityThread) as Context?
//            } catch (e: Exception) {
//                e.printStackTrace()
//                mContext
//            }
//            return context
//        }
}
