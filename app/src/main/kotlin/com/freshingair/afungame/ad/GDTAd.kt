package com.freshingair.afungame.ad

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.freshingair.afungame.activity.BrowserActivity
import com.freshingair.afungame.R
import com.freshingair.afungame.activity.SplashActivity
import com.freshingair.afungame.others.DialogUtils
import com.freshingair.afungame.others.DialogUtils.setAlertDialog
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.managers.GDTAdSdk
import com.qq.e.comm.managers.GDTAdSdk.OnStartListener
import com.qq.e.comm.util.AdError


object GDTAd {
    private const val APP_ID: String = "1101152570"
    private const val SPLASH_ID: String = "9093517612222759"
    private const val REWARD_VIDEO_AD_ID_SUPPORT_H: String = "2090845242931421" //支持竖版出横版视频
    // private const val REWARD_VIDEO_AD_ID_UN_SUPPORT_H: String = "4000898212322043" //不支持竖版出横版视频
    private const val TAG = "广点通广告 SDK"
    private var isViewedAd = false
    fun initSDK(context: Context) {
        GDTAdSdk.initWithoutStart(context, APP_ID) // 该接口不会采集用户信息
        // 调用initWithoutStart后请尽快调用start，否则可能影响广告填充，造成收入下降
        GDTAdSdk.start(object : OnStartListener {
            override fun onStartSuccess() {
                // 推荐开发者在onStartSuccess回调后开始拉广告
                Log.i(TAG, "SDK初始化成功")
            }

            override fun onStartFailed(e: Exception) {
                e.printStackTrace()
            }
        })
    }
    fun splashAd(context: Context, container: ViewGroup?) {
        // 创建开屏广告实例
        val splashAD = SplashAD(context, SPLASH_ID, object : SplashADListener {
            override fun onADDismissed() {
                // 广告关闭，进入主界面
                Log.i(TAG, "广告关闭")
                SplashActivity.goToMainActivity(context)
            }

            override fun onNoAD(adError: AdError) {
                // 广告加载失败，也进入主界面
                Log.e(
                    TAG,
                    "广告加载失败，错误信息：${adError.errorMsg}(${adError.errorCode})"
                )
                SplashActivity.goToMainActivity(context)
            }

            override fun onADPresent() {
                // 广告成功展示
                Log.i(TAG, "广告成功展示")
            }

            override fun onADClicked() {
                // 广告被点击
                Log.i(TAG, "用户点击了广告")
            }

            override fun onADTick(millisUntilFinished: Long) {
            }

            override fun onADExposure() {
                // 广告曝光
                Log.i(TAG, "广告曝光")
            }

            override fun onADLoaded(l: Long) {
                // 广告加载成功（开屏广告加载成功后会立即展示，此回调可能不会触发）
                Log.i(TAG, "广告加载成功")
            }
        })
        // 拉取并展示广告，传入广告容器和自定义的跳过按钮（可选）
        splashAD.fetchAdOnly()
        splashAD.showAd(container)
        // 如果不需要自定义跳过按钮，可以使用下面这个方法
        // splashAD.fetchAndShowIn(container);
    }
    fun rewardAd(context: Context, loadingDialog: AlertDialog){
        // 仅展示部分代码，完整代码请参考 Demo 工程
        // 1.加载广告，先设置加载上下文环境和条件
        // 如果想静音播放，请使用5个参数的构造函数，且volumeOn传false即可
        var rewardVideoAD : RewardVideoAD? = null
        rewardVideoAD = RewardVideoAD(context, REWARD_VIDEO_AD_ID_SUPPORT_H,object : RewardVideoADListener {
            override fun onADLoad() {
                rewardVideoAD?.hasShown()?.let {
                    if (!it && rewardVideoAD!!.isValid) {
                        //广告展示检查2：当前广告数据还没有展示过
                        //广告展示检查3：展示广告前判断广告数据未过期
                        rewardVideoAD!!.showAD()
                    }
                }
            }
            override fun onVideoCached() {}
            override fun onADShow() {
                loadingDialog.dismiss()
            }
            override fun onADExpose() {}
            override fun onReward(p0: Map<String?, Any?>?) {
                isViewedAd = true
            }
            override fun onADClick() {}
            override fun onVideoComplete() {}
            override fun onADClose() {
                if(isViewedAd) {
                    DialogUtils.getAlertDialog(context, true)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("一般性提示")
                        .setMessage("还需再观看一次。😜")
                        .setPositiveButton("确定") { _, _ ->
                            // 确定逻辑

                            val view = LayoutInflater.from(context)
                                .inflate(R.layout.layout_dialog_loading, null)

                            val loadingDialog: AlertDialog = AlertDialog.Builder(context)
                                .setIcon(R.drawable.ic_info)
                                .setView(view)
                                .setCancelable(false)
                                .create()

                            if (loadingDialog.isShowing) return@setPositiveButton

                            loadingDialog.show()
                            setAlertDialog(context, loadingDialog)

                            KsAd.rewardAd(context, loadingDialog)
                        }
                        .setCancelable(false)
                        .show()
                }
            }
            override fun onError(p0: AdError?) {
                Log.e(TAG, "广告加载失败，错误信息：${p0?.errorMsg}(${p0?.errorCode})")
                loadingDialog.dismiss()
                val intent = Intent(context, BrowserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                (context as Activity).finish()
            }
        })

        rewardVideoAD.loadAD()
    }

}