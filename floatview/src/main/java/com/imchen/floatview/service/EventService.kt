package com.imchen.floatview.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.*

/**
 * Created by imchen on 2017/12/11.
 */
class EventService : Service() {
    override fun onBind(intent: Intent?): IBinder {
//        collectEvent("getevent")
        toDeskTop()
        Thread(Runnable { execShellSu("/data/local/tmp/test.txt") }).start()
        return MyBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    internal inner class MyBinder : Binder() {
    }

    private fun collectEvent(shell: String) {
        var proc: Process = Runtime.getRuntime().exec(shell + "\n")
        var ins: InputStream = proc.inputStream
        var reader: BufferedReader = BufferedReader(InputStreamReader(ins))
        var buff: StringBuffer
        var line: String? = null
        while ((reader.readLine()) != null) {
            Log.d("shell:", reader.readLine().toString())
        }
    }

    private fun execShellSu(filePath: String) {
        val file = File(filePath)
        var proc: Process? = null
        if (!file.exists()) {
            return
        }
        if (proc == null) {
            proc = Runtime.getRuntime().exec("su \n")
        }
        var ops = proc!!.outputStream
        var buff = BufferedReader(FileReader(file))
        var line: String? = null
        var index=0
        var sleepTime:Long=0

        ops.write("chmod 777 /dev/input/*".toByteArray())
        while (buff.readLine().apply { line = this } != null) {
            index=line!!.lastIndexOf("]")
            sleepTime= (line!!.substring(1,index)).toLong()
            line=line!!.substring(index+1)
            if (sleepTime!=0L){
                Thread.sleep(sleepTime)
            }
            Log.d("EventService", "shell ->" + line)
            ops.write(line!!.toByteArray())
//            Log.d("EventService",proc.waitFor().toString())
        }
    }

    fun toDeskTop(){
        val intent:Intent =Intent(Intent.ACTION_MAIN,null)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

}