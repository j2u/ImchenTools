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
import android.widget.TextView
import android.widget.Toast
import com.imchen.floatview.service.EventService
import com.imchen.floatview.view.FloatBallView
import java.lang.reflect.Field

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var showViewBtn: Button? = null
    private var bindBtn: Button? = null

    companion object {
        private var resultTv: TextView? = null
        val mHandler: Handler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                when (msg!!.what) {
                    0x11111 -> {
                        var result:String = msg.obj as String
                        resultTv!!.setText(result)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //6.x 必须这样开启权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applyCommonPermission(applicationContext)
        }
        initView()
    }


    private fun initView() {
        showViewBtn = findViewById(R.id.btn_show_float_view)
        bindBtn = findViewById(R.id.btn_bind_service)
        resultTv = findViewById(R.id.tv_shell_result)

        showViewBtn?.setOnClickListener(this)
        bindBtn?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_show_float_view -> {
                var manager = FloatViewManager.getInstance()
                var view = FloatBallView(this)
                view.initView()
                manager.showView(view, view.createLayoutParam())
            }
            R.id.btn_exec_shell->{

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
            val field: Field = clazz.java.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            val intent = Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (e: Exception) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    private fun startRecordService() {
        val intent = Intent()
        intent.setClass(this, EventService::class.java)
        bindService(intent, MyServiceConnection(), Context.BIND_AUTO_CREATE)
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
