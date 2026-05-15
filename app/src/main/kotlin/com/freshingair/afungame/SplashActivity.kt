package com.freshingair.afungame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_splash)
        
        val broadcastCenter = ViewModelProvider(this)[SplashBroadcastCenter::class.java]
        // 观察消息变化（自动感知生命周期，无需手动注销）
        broadcastCenter.messageEvent.observe(this) { message ->
            // 处理接收到的消息
            //Log.d("Receiver", "收到消息：$message")
            if (message == "FINISH_ACTIVITY") {
                findViewById<ViewGroup?>(R.id.SplashAdContainer)!!.removeAllViews()
                finish()
            }
        }

        val slideIv = findViewById<FadeImageView>(R.id.SplashImage)
        slideIv.setImagePaths(
            paths = listOf("warning_1.jpg", "warning_2.jpg"),
            fadeInMs = 1000,
            stayMs = 3000,
            fadeOutMs = 1000,
            loop = false
        )
        
        // 正确回调（无报错）
        slideIv.fadeListener = object : FadeImageView.FadeListener {
            override fun onFadeInStart(path: String, index: Int) {}
            override fun onFadeInEnd(path: String, index: Int) {}
            override fun onFadeOutStart(path: String, index: Int) {}
            override fun onFadeOutEnd(path: String, index: Int) {}
            override fun onPageChange(nextIndex: Int) {}
            override fun onPlayAllEnd() { 
                if(BuildConfig.DEBUG) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                else{
                    GDTAd.initSDK(this@SplashActivity)
                    KsAd.initSDK(this@SplashActivity)
                    showAd()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun showAd(){
        val bool = listOf(true, false).random()
        if (bool) {
            GDTAd.splashAd(this, findViewById(R.id.SplashAdContainer))
        } else {
            KsAd.splashAd(this, findViewById(R.id.SplashAdContainer))
        }
    }

    companion object {
        @JvmStatic
        fun goToMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            // 将 context 强转为 SplashActivity 实例
            val activity = context as? SplashActivity ?: return
            // 通过实例调用 send() 方法
            activity.goToMainActivity()
        }
    }

    fun goToMainActivity(){
        ViewModelProvider(this)[SplashBroadcastCenter::class.java].sendMessage("FINISH_ACTIVITY")
    }
}