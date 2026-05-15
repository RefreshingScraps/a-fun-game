package com.freshingair.afungame.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.freshingair.afungame.BuildConfig
import com.freshingair.afungame.R
import com.freshingair.afungame.ad.GDTAd
import com.freshingair.afungame.autoFullScreen
import com.freshingair.afungame.others.SPUtils.readVersionNameFromAssets
import com.freshingair.afungame.others.DialogUtils
import com.freshingair.afungame.others.SPUtils
import com.plattysoft.leonids.ParticleSystem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // 动画/粒子对象 全局声明，方便生命周期管理
    private var congratulationsAnim: LottieAnimationView? = null
    private lateinit var particleSystem1: ParticleSystem
    private lateinit var particleSystem2: ParticleSystem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_main)

        // 初始化控件
        initViews()
        // 初始化更新提示
        initUpdateDialog()
    }

    /**
     * 初始化按钮点击事件
     */
    private fun initViews() {
        // 进入游戏按钮
        findViewById<Button>(R.id.go).setOnClickListener { handleStartGame() }
        // 设置按钮
        findViewById<Button>(R.id.settings).setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }

    /**
     * 处理开始游戏逻辑（拆分核心逻辑，代码更清晰）
     */
    private fun handleStartGame() = if (BuildConfig.DEBUG) startBrowserActivity() else showAdTipDialog()

    /**
     * 展示广告提示弹窗
     */
    private fun showAdTipDialog() {
        DialogUtils.getAlertDialog(this, true)
            .setIcon(R.drawable.ic_info)
            .setTitle("一般性提示")
            .setMessage("为避免频繁请求服务器，您需要观看一个视频，待获得奖励后才能进入游戏。")
            .setPositiveButton("确定") { _, _ ->
                // 展示加载弹窗
                showLoadingDialog()
            }.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }.setCancelable(true)
            .create()
            .apply {
                show()
                DialogUtils.setAlertDialog(this@MainActivity, this)
            }
    }

    /**
     * 展示加载弹窗并加载广告
     */
    private fun showLoadingDialog() {
        val loadingView = LayoutInflater.from(this)
            .inflate(R.layout.layout_dialog_loading, null)

        val loadingDialog = DialogUtils.getAlertDialog(this, true)
            .setIcon(R.drawable.ic_info)
            .setView(loadingView)
            .setCancelable(false)
            .create()
            .apply {
                if (!isShowing) {
                    show()
                    DialogUtils.setAlertDialog(this@MainActivity, this)
                }
            }

        // 加载激励广告
        GDTAd.rewardAd(this, loadingDialog)
    }

    /**
     * 跳转到游戏网页界面
     */
    private fun startBrowserActivity() {
        val intent = Intent(this, BrowserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    /**
     * 初始化更新弹窗+特效
     */
    private fun initUpdateDialog() {
        val isConfirm = SPUtils.getBoolean(this, "isConfirmUpdateDialog", false)
        if (!isConfirm) {
            // 展示更新日志
            val versionName = readVersionNameFromAssets(this)
            DialogUtils.showMarkdownDialog(this, "version/update-catalog.md", "欢迎使用 $versionName") { dialog, _ ->
                // 播放粒子特效
                particleSystem1 = ParticleSystem(this, 80, R.drawable.confeti2, 10000)
                    .setSpeedModuleAndAngleRange(0f, 0.1f, 180, 180)
                    .setRotationSpeed(144f)
                    .setAcceleration(0.000017f, 90)
                    .apply { emit(findViewById(R.id.emiter_top_left), 8) }

                particleSystem2 = ParticleSystem(this, 80, R.drawable.confeti3, 10000)
                    .setSpeedModuleAndAngleRange(0f, 0.1f, 0, 0)
                    .setRotationSpeed(144f)
                    .setAcceleration(0.000017f, 90)
                    .apply { emit(findViewById(R.id.emiter_top_right), 8) }
                // 播放Lottie庆祝动画
                congratulationsAnim = findViewById<LottieAnimationView>(R.id.lottie_congratulations).apply {
                    visibility = View.VISIBLE
                    setAnimation("version/congratulations.json")
                    repeatMode = LottieDrawable.RESTART
                    playAnimation()
                }
                dialog.dismiss()

                lifecycleScope.launch {
                    delay(3000) // 延迟 3 秒
                    particleSystem1.cancel()
                    particleSystem2.cancel()
                    findViewById<View>(R.id.emiter_top_left).visibility = View.GONE
                    findViewById<View>(R.id.emiter_top_right).visibility = View.GONE
                    congratulationsAnim?.cancelAnimation()
                    congratulationsAnim?.visibility = View.GONE
                    congratulationsAnim = null
                }
            }
            SPUtils.putBoolean(this, "isConfirmUpdateDialog", !BuildConfig.DEBUG)
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止粒子系统
        particleSystem1.cancel()
        particleSystem2.cancel()
        // 停止并释放Lottie动画
        congratulationsAnim?.cancelAnimation()
        congratulationsAnim = null
    }

}