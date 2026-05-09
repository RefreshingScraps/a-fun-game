package com.freshingair.afungame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.go).setOnClickListener {
            if(!BuildConfig.DEBUG) {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_info)
                    .setTitle("一般性提示")
                    .setMessage("为避免频繁请求服务器，您需要观看一个视频，待获得奖励后才能进入游戏。")
                    .setPositiveButton("确定") { _, _ ->
                        // 确定逻辑

                        val view = LayoutInflater.from(this)
                            .inflate(R.layout.layout_dialog_loading, null)

                        val loadingDialog: AlertDialog = AlertDialog.Builder(this)
                            .setIcon(R.drawable.ic_info)
                            .setView(view)
                            .setCancelable(false)
                            .create()

                        if (loadingDialog.isShowing) return@setPositiveButton

                        loadingDialog.show()

                        GDTAd.rewardAd(this, loadingDialog)
                    }.setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .show()
            } else{
                val intent = Intent(this, BrowserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        findViewById<Button>(R.id.settings).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}