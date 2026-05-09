package com.freshingair.afungame

import android.app.Application
import android.content.Context

class MyApplication: Application() {
    override fun onCreate(){
        super.onCreate()
//        AppCrashHandler.init(this)
    }
}