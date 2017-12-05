package com.imchen.floatview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.imchen.floatview.view.FloatBallView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        var manager = FloatViewManager.getInstance()
        var view = FloatBallView(applicationContext)
        view.initView()
        manager.showView(view, view.createLayoutParam())
    }
}
