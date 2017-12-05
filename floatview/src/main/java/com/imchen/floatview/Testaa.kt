package com.imchen.floatview

import android.content.Context
import android.view.WindowManager

/**
 * Created by imchen on 2017/12/5.
 */

class Testaa {

    private var windowManager: WindowManager? = null
    private val mContext: Context? = null
    fun test() {
        windowManager = mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}
