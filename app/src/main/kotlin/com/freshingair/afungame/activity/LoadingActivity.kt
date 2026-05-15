package com.freshingair.afungame.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.ProgressBar
import com.freshingair.afungame.BuildConfig
import com.freshingair.afungame.FadeImageView
import com.freshingair.afungame.R
import com.freshingair.afungame.autoFullScreen
import java.util.Random
import com.unity3d.player.UnityPlayerActivity

class LoadingActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private val mHandler = Handler()
    private val random = Random()
    private val progressRunnable = object : Runnable {
        override fun run() {
            val add = random.nextInt(5) + 1
            var current = progressBar.progress
            current += add
            if (current >= progressBar.max) {
                current = 0
                stopRandomProgress()
            }
            progressBar.progress = current
            mHandler.postDelayed(this, 200)
        }
    }
    
    fun startRandomProgress() {
        mHandler.removeCallbacks(progressRunnable)
        mHandler.post(progressRunnable)
    }
    fun stopRandomProgress() {
        mHandler.removeCallbacks(progressRunnable)
        val intent = Intent(this, UnityPlayerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_loading)
        
        val slideIv = findViewById<FadeImageView>(R.id.fadeImgView)
        slideIv.setImagePaths(
            paths = listOf(
            "img1.jpg", "img2.jpg", "img3.png", "img4.jpg"),
            fadeInMs = 800,
            stayMs = 3000,
            fadeOutMs = 0,
            loop = true
        )

        // 正确回调（无报错）
        slideIv.fadeListener = object : FadeImageView.FadeListener {
            override fun onFadeInStart(path: String, index: Int) {}
            override fun onFadeInEnd(path: String, index: Int) {}
            override fun onFadeOutStart(path: String, index: Int) {}
            override fun onFadeOutEnd(path: String, index: Int) {}
            override fun onPageChange(nextIndex: Int) {}
            override fun onPlayAllEnd() {}
        }
        
        progressBar = findViewById(R.id.progressBarHorizontal)
        startRandomProgress()
        if(BuildConfig.DEBUG) progressBar.max = 50
    }
}
