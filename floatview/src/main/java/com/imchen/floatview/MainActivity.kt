package com.imchen.floatview

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.imchen.floatview.service.EventService
import com.imchen.floatview.ui.FloatBallView
import java.io.*
import java.lang.Process
import java.lang.reflect.Field

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var showViewBtn: Button? = null
    private var bindBtn: Button? = null
    private var execShellBtn: Button? = null
    private var shellEt: EditText? = null


    companion object {
        private var resultTv: TextView? = null
        private var mContext:Context? =null
        val mHandler: Handler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                when (msg!!.what) {
                    0x11111 -> {
                        var result: String = msg.obj as String
                        resultTv!!.setText(result)
                    }
                    0x00002 -> {
                        Toast.makeText(mContext, "result code: " + msg.obj + " please check permission!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext=applicationContext
        //6.x 必须这样开启权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applyCommonPermission(applicationContext)
        }
        initView()
    }


    private fun initView() {
        showViewBtn = findViewById(R.id.btn_show_float_view)
        bindBtn = findViewById(R.id.btn_bind_service)
        execShellBtn = findViewById(R.id.btn_exec_shell)
        shellEt = findViewById(R.id.et_shell)
        resultTv = findViewById(R.id.tv_shell_result)

        showViewBtn?.setOnClickListener(this)
        bindBtn?.setOnClickListener(this)
        execShellBtn?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_show_float_view -> {
                var manager = FloatViewManager.getInstance()
                var view = FloatBallView(this)
                view.initView()
                manager.showView(view, view.createLayoutParam())
            }
            R.id.btn_exec_shell -> {
                shellThread().start()
            }
            R.id.btn_bind_service -> {
                startRecordService()
            }
        }
    }

    private fun initPermission(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(permissions[0])) == PackageManager.PERMISSION_GRANTED)
                return
            requestPermissions(permissions, 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MainActivity", "grantResult" + permissions[0] + " " + grantResults[0])
    }

    private fun applyCommonPermission(context: Context) {
        try {
            val clazz = Settings::class
            val field: Field = clazz.java.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION")
            val intent = Intent(field.get(null).toString())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    private fun startRecordService() {
        val intent = Intent()
        intent.setClass(this, EventService::class.java)
        bindService(intent, MyServiceConnection(), Context.BIND_AUTO_CREATE)
    }

    internal inner class shellThread : Thread() {
        override fun run() {
            var shellText = shellEt?.text.toString()
            Log.d("MainActivity", "exec shell: ->" + shellText)
//            var sm=System.getSecurityManager()
//            sm.checkExec(shellText)
            var proc: Process? = null
            if (proc == null) {
                proc = Runtime.getRuntime().exec("su \n")
            }

            var osp = proc?.outputStream
            var dos = DataOutputStream(osp)
            dos.writeBytes(shellText + " \n")
            dos.flush()
            dos.close()
            var baos = ByteArrayOutputStream()
            var ips = proc?.inputStream
            var resultCode = proc?.waitFor()
            if (resultCode == 1) {
//                Toast.makeText(this@MainActivity, "result code: " + resultCode + " please check permission!", Toast.LENGTH_SHORT).show
                var msg:Message= Message()
                msg.what=0x00002
                msg.obj=resultCode
                mHandler.sendMessage(msg)
            }
            Log.d("MainActivity","resultcode: "+resultCode)
            var reader:BufferedReader= BufferedReader(InputStreamReader(ips))
            while (reader.readLine()!=null){
                Log.d("MainActivity","line-> "+reader.readLine())
            }
//            var size = ips!!.read()
//            while ((ips!!.read()) != -1) {
////                if (size > 512) {
////                    baos.write(512)
////                    size += 512
////                } else {
//                    baos.write(ips!!.read())
////                }
////                Log.d("MainActivity","size->"+baos.toString())
//            }
//            Log.d("MainActivity", "shell:->" + resultCode + " command: " + shellEt?.text.toString() + "\n" + baos.toString())

        }
    }

    internal inner class MyServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("MainActivity", "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("MainActivity", "onServiceConnected")
        }
    }

}
