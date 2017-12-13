package com.imchen.floatview.ui

import android.content.Context
import android.widget.Toast

/**
 * Created by imchen on 2017/12/13.
 */
fun Context.showToast(message: String): Toast {
    var toast:Toast=Toast.makeText(this,message,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}