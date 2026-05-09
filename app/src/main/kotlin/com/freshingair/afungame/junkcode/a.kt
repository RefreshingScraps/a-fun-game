package com.freshingair.afungame.junkcode

import ads_mobile_sdk.al
import android.app.Activity
import android.webkit.WebView
import android.widget.FrameLayout
import com.freshingair.afungame.junkcode.s2f.stringToFile
import com.kuaishou.weapon.p0.aj
import com.kuaishou.weapon.p0.dd.ad
import com.kuaishou.weapon.p0.dd.ay
import com.kwad.components.ad.draw.a.a.aK
import com.kwad.components.ad.draw.a.d.aL
import com.kwad.components.ad.e.b.aA
import com.kwad.components.ad.e.b.aC
import com.kwad.components.ad.e.b.aD
import com.kwad.components.core.innerEc.logger.a.aN
import com.kwad.components.core.request.model.b.a.aM
import com.kwad.components.core.webview.jshandler.ah
import com.kwad.components.core.webview.jshandler.au
import com.kwad.framework.filedownloader.f.c.aX
import com.kwad.framework.filedownloader.r.aW
import com.kwad.sdk.core.b.a.aa
import com.kwad.sdk.core.b.a.ac
import com.kwad.sdk.core.b.a.ae
import com.kwad.sdk.core.b.a.ag
import com.kwad.sdk.core.b.a.ai
import com.kwad.sdk.core.b.a.ak
import com.kwad.sdk.core.b.a.am
import com.kwad.sdk.core.b.a.ao
import com.kwad.sdk.core.b.a.aq
import com.kwad.sdk.core.b.a.at
import com.kwad.sdk.core.b.a.av
import com.kwad.sdk.core.b.a.ax
import com.kwad.sdk.core.b.a.az
import com.kwad.sdk.core.response.b.a.aB
import com.kwad.sdk.core.response.b.a.aE
import com.kwad.sdk.core.response.b.a.aF
import com.kwad.sdk.core.response.b.a.aG
import com.kwad.sdk.core.response.b.a.aH
import com.kwad.sdk.core.response.b.a.aI
import com.kwad.sdk.core.response.b.a.aJ
import com.kwad.sdk.core.response.b.a.aY
import com.kwad.sdk.core.response.b.a.aZ
import com.kwad.sdk.core.response.model.AdInfo
import com.kwad.sdk.core.response.model.AdTemplate
import com.kwad.sdk.utils.au.aw
import com.kwad.sdk.utils.y.ab
import com.unity3d.player.a1
import com.kwad.sdk.core.webview.b
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

fun a(activity: Activity) {
    val e: a1
    val adInfo = AdInfo().apply {
        adBaseInfo.appPackageName = "com.xxx.app"       // 包名
        adBaseInfo.appName = "应用名称"                 // 应用名
        adConversionInfo.h5Url = "https://xxx.com"      // H5 链接
        adConversionInfo.appDownloadUrl = "https://xxx.apk" // 下载链接
    }
    aa()
    ab(stringToFile(activity, "text"))
    ac()
    ad
    ae()
    // af(1000L)
    ag()
    ah()
    ai()
    aj(activity)
    ak()
    al(null, Continuation<Any>(EmptyCoroutineContext) { _ -> })
    am()
    ao()
    aq()
    at()
    au(b().apply {
            acu = WebView(activity)
            adn = FrameLayout(activity)
            mScreenOrientation = 0
            adTemplate = AdTemplate()
    })
    av()
    aw(activity, "text")
    ax()
    ay
    az()
    // 报错：需要函数调用 → 加()
    aA()
    aB(adInfo)
    aC()
    aD()
    aE(adInfo)
    aF(adInfo)
    aG(adInfo)
    aH(adInfo)
    aI(adInfo)
    aJ(adInfo)
    aK()
    aL()
    aM(activity)
    aN(AdTemplate())
    aW(activity)
    aX(activity)
    aY(adInfo)
    aZ(adInfo)
}