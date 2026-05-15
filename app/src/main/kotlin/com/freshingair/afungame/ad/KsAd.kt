package com.freshingair.afungame.ad

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.freshingair.afungame.activity.BrowserActivity
import com.freshingair.afungame.BuildConfig
import com.freshingair.afungame.activity.SplashActivity.Companion.goToMainActivity
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsLoadManager.RewardVideoAdListener
import com.kwad.sdk.api.KsLoadManager.SplashScreenAdListener
import com.kwad.sdk.api.KsRewardVideoAd
import com.kwad.sdk.api.KsRewardVideoAd.RewardAdInteractionListener
import com.kwad.sdk.api.KsScene
import com.kwad.sdk.api.KsSplashScreenAd
import com.kwad.sdk.api.KsSplashScreenAd.SplashScreenAdInteractionListener
import com.kwad.sdk.api.KsVideoPlayConfig
import com.kwad.sdk.api.SdkConfig


object KsAd {
    private const val APP_ID: String = "90009"
    private const val SPLASH_ID: Long = 4000000042L
    private const val REWARD_ID: Long = 90009001L
    private const val TAG = "快手广告 SDK"
    fun initSDK(context: Context) {
        KsAdSDK.init(
            context, SdkConfig.Builder()
                .appId(APP_ID) // 测试aapId，请联系快⼿平台申请正式AppId，必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(BuildConfig.DEBUG) // 是否开启sdk 调试⽇志  可选
                .build()
        )
        KsAdSDK.start()
    }
    fun splashAd(context: Context, container: ViewGroup) {
        val adRequestManager = KsAdSDK.getLoadManager()
        val scene = KsScene.Builder(SPLASH_ID).build() // 此为测试posId，请联系快⼿平台申请正式posId
        if (adRequestManager != null) {
            adRequestManager.loadSplashScreenAd(scene, object : SplashScreenAdListener {
                override fun onError(code: Int, msg: String?) {
                    Log.e(TAG, "开屏⼴告请求失败，错误信息：$code($msg)")
                    goToMainActivity(context)
                }

                override fun onRequestResult(adNumber: Int) {
                    Log.i(TAG, "开屏⼴告⼴告请求填充 $adNumber")
                }

                override fun onSplashScreenAdLoad(splashScreenAd: KsSplashScreenAd?) {
                    //SplashAd.ksSplashScreenAd 为静态变量， 保存splashScreenAd⽤户⼩窗模式 SplashAd.ksSplashScreenAd = splashScreenAd;
                    //你可以选择View接⼊或者Frament接⼊ addFragment(KsSplashScreenAd splashScreenAd)
                    if (splashScreenAd != null) {
                        val view = splashScreenAd.getView(
                            context,
                            object : SplashScreenAdInteractionListener {
                                override fun onAdClicked() {
                                    Log.i(TAG, "开屏⼴告点击")
                                    //onAdClick 会吊起h5或者应⽤商店。 不直接跳转，等返回后再跳转。 mGotoMainActivity = true;
                                    //点击不出发显示miniWindow SplashAd.ksSplashScreenAd = null;
                                }

                                override fun onAdShowError(code: Int, extra: String?) {
                                    Log.i(TAG, "开屏⼴告显示错误 $code extra $extra")
                                    goToMainActivity(context)
                                    //出错不出发显示miniWindown SplashAd.ksSplashScreenAd = null;
                                }

                                override fun onAdShowEnd() {
                                    Log.i(TAG, "开屏⼴告展示完毕")
                                    goToMainActivity(context)
                                }

                                override fun onAdShowStart() {
                                    Log.i(TAG, "开屏⼴告开始展示")
                                }
                                override fun onSkippedAd() {
                                    Log.i(TAG, "⽤户跳过开屏⼴告")
                                    goToMainActivity(context)
                                }
                                override fun onDownloadTipsDialogShow() {}
                                override fun onDownloadTipsDialogDismiss() {}
                                override fun onDownloadTipsDialogCancel() {}
                            })
                        container.removeAllViews()
                        view.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        container.addView(view)
                    }
                }
            })
        }
    }
    fun rewardAd(context: Context, loadingDialog: AlertDialog){
        var mRewardVideoAd: KsRewardVideoAd? = null
        val adScene = KsScene.Builder(REWARD_ID).build() // 此为测试posId，请联系 快⼿平台申请正式posId
        KsAdSDK.getLoadManager().loadRewardVideoAd(adScene, object : RewardVideoAdListener {
            override fun onError(code: Int, msg: String?) {
                Toast.makeText(context,"激励视频⼴告请求失败$code$msg", Toast.LENGTH_LONG).show()
                goToLoadingActivity(context)
            }
            override fun onRewardVideoResult(p0: List<KsRewardVideoAd?>?) {}
            override fun onRewardVideoAdLoad(adList: MutableList<KsRewardVideoAd?>?) {
                if (!adList.isNullOrEmpty()) {
                    mRewardVideoAd = adList[0]
                    Toast.makeText(context,"激励视频⼴告请求成功", Toast.LENGTH_LONG).show()
                    loadingDialog.dismiss()
                }
            }
        })

        val videoPlayConfig = KsVideoPlayConfig.Builder()
            .showLandscape(true) // 横屏播放
            .build()
        if (mRewardVideoAd != null && mRewardVideoAd!!.isAdEnable) {
            mRewardVideoAd!!.setRewardAdInteractionListener(
                object : RewardAdInteractionListener {
                    override fun onAdClicked() {}
                    override fun onPageDismiss() {}
                    override fun onVideoPlayError(code: Int, extra: Int) {
                        loadingDialog.dismiss()
                        goToLoadingActivity(context)
                    }

                    override fun onVideoPlayEnd() {}
                    override fun onVideoSkipToEnd(p0: Long) {}
                    override fun onVideoPlayStart() {}
                    override fun onRewardVerify() {
                        // 激励视频⼴告获取激励
                        goToLoadingActivity(context)
                    }

                    override fun onRewardVerify(p0: Map<String?, Any?>?) {
                        // 激励视频⼴告获取激励
                        goToLoadingActivity(context)
                    }

                    override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {}
                    override fun onExtraRewardVerify(p0: Int) {}
                })
        }
        mRewardVideoAd?.showRewardVideoAd(context as Activity?, videoPlayConfig)
    }
    fun goToLoadingActivity(context: Context){
        val intent = Intent(context, BrowserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        (context as Activity).finish()
    }
}