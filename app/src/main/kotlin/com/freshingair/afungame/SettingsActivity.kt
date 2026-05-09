package com.freshingair.afungame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var s1: SwitchCompat
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_settings)

        s1 = findViewById(R.id.switch_one)
        
        // 读取上次保存状态
        s1.isChecked = SPUtils.getBoolean(this, "key_s1", false)
        
        // 开关变化自动保存
        s1.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.putBoolean(this, "key_s1", isChecked)
            throw RuntimeException("何意味")
        }
    }
}