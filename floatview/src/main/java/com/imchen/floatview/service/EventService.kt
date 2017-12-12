package com.imchen.floatview.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by imchen on 2017/12/11.
 */
class EventService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        collectEvent("getevent")
        return MyBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    internal inner class MyBinder : Binder() {
    }

    private fun collectEvent(shell: String) {
        var proc: Process = Runtime.getRuntime().exec(shell+"\n")
        var ins: InputStream = proc.inputStream
        var reader: BufferedReader = BufferedReader(InputStreamReader(ins))
        var buff: StringBuffer
        var line: String? = null
        while ((reader.readLine()) != null) {
            Log.d("shell:", reader.readLine().toString())
        }
    }

}