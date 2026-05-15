package com.freshingair.afungame

import android.app.Dialog
import android.hardware.SensorManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.plattysoft.leonids.ParticleSystem
import java.util.Random

class WinnerActivity : AppCompatActivity() {
    private var winnerDialog: Dialog? = null
    private var soundPool: SoundPool? = null
    private var soundId = 0
    private val handler = Handler()
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // 初始化音效
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundId = soundPool!!.load(this, R.raw.win_sound, 1)

        // 【不定时随机触发】1~10秒随机弹出
        startRandomWinner()
    }

    // 随机时间弹出中奖特效
    private fun startRandomWinner() {
        val delay = 1000 + random.nextInt(9000) // 1~10秒
        handler.postDelayed({ this.showWinnerDialog() }, delay.toLong())
    }

    // 超夸张中奖弹窗
    private fun showWinnerDialog() {
        winnerDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        winnerDialog?.setContentView(R.layout.dialog_winner)
        winnerDialog!!.setCancelable(false)
        winnerDialog!!.show()

        // 震动
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(800)

        // 播放中奖音效
        soundPool!!.play(soundId, 1f, 1f, 0, 0, 1f)

        // 卡片动画：放大+旋转（夸张）
        val card = winnerDialog!!.findViewById<LinearLayout?>(R.id.card)
        val scale = ScaleAnimation(
            0f, 1.3f, 0f, 1.3f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scale.duration = 600
        scale.fillAfter = true

        val rotate = RotateAnimation(
            0f, 15f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 600
        card?.let {
            it.startAnimation(scale)
            it.startAnimation(rotate)
        }
        // Lottie 闪光
        val light = winnerDialog!!.findViewById<LottieAnimationView?>(R.id.lottie_light)
        light?.let {
            it.visibility = View.VISIBLE
            it.setAnimation("win-light.json") // 去Lottie官网下载闪光json放assets
            it.repeatMode = LottieDrawable.RESTART
            it.playAnimation()
        }
        // 金币/彩带乱飞（超级夸张）
        ParticleSystem(this, 100, R.drawable.coin, 3000)
            .setSpeedRange(0.5f, 2f)
            .oneShot(card, 100)

        ParticleSystem(this, 80, R.drawable.ribbon, 4000)
            .setSpeedRange(0.4f, 1.8f)
            .oneShot(card, 80)

        // 关闭按钮
        val btnClose = winnerDialog!!.findViewById<Button?>(R.id.btn_close)
        btnClose?.setOnClickListener { _: View? ->
            winnerDialog!!.dismiss()
            startRandomWinner() // 关闭后继续随机弹出
        }
    }
}