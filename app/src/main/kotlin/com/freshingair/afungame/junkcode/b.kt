package com.freshingair.afungame.junkcode

import ads_mobile_sdk.bg
import ads_mobile_sdk.bl
import ads_mobile_sdk.br
import ads_mobile_sdk.bt
import ads_mobile_sdk.bx
import ads_mobile_sdk.bs
import ads_mobile_sdk.h51
import ads_mobile_sdk.ln
import ads_mobile_sdk.xw
import android.app.Activity
import com.kuaishou.weapon.p0.bf
import com.kuaishou.weapon.p0.dd.bb
import com.kwad.components.ad.feed.d.bW
import com.kwad.components.core.pfmonitor.model.b.be
import com.kwad.components.core.t.f.bk
import com.kwad.components.core.webview.jshandler.bh
import com.kwad.framework.filedownloader.r.bU
import com.kwad.sdk.api.loader.aa.bu
import com.kwad.sdk.c.a.a.bA
import com.kwad.sdk.collector.j.bH
import com.kwad.sdk.commercial.a.a.bE
import com.kwad.sdk.core.b.a.bj
import com.kwad.sdk.core.config.b.bK
import com.kwad.sdk.core.config.item.c.bN
import com.kwad.sdk.core.download.b.b.bS
import com.kwad.sdk.core.network.idc.b.bT
import com.kwad.sdk.core.report.q.bX
import com.kwad.sdk.core.response.b.a.bC
import com.kwad.sdk.core.response.model.AdInfo
import com.kwad.sdk.core.response.model.AdTemplate
import com.kwad.sdk.utils.ai.bc
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

fun b(activity: Activity) {
    val adInfo = AdInfo().apply {
        adBaseInfo.appPackageName = "com.xxx.app"       // 包名
        adBaseInfo.appName = "应用名称"                 // 应用名
        adConversionInfo.h5Url = "https://xxx.com"      // H5 链接
        adConversionInfo.appDownloadUrl = "https://xxx.apk" // 下载链接
    }
    bb
    bc(1000L)
    be("text")
    bf()
    bg(h51(Any()))
    bh {}
    bj()
    bk("text")
    bl(null, Continuation<Any>(EmptyCoroutineContext) { _ -> })
    ln.bo
    @Suppress("DEPRECATION")
    com.kwad.sdk.utils.bq()
    br()
    bs.a()
    bs.b()
    bt()
    bu(activity)
    bx(xw())

    bA(activity)
    bC(adInfo)
    bE(AdTemplate())
    bH(activity)
    bK(activity)
    bN(activity)
    bS(activity)
    bT(activity)
    bU("text")
    // bV.setKsAdLabel(null)
    bW()
    bX(activity)
}