package com.freshingair.afungame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.freshingair.afungame.DialogUtils.DialogUtils
import com.freshingair.afungame.DialogUtils.setAlertDialog

class BrowserActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoFullScreen()
        setContentView(R.layout.activity_browser)
        webView = findViewById(R.id.webview)
        initWebView()
        initBackPress()
    }
     // 新版返回事件处理
    private fun initBackPress() {
        val backCallback = object : OnBackPressedCallback(true) {
             override fun handleOnBackPressed() {
                 if (webView.canGoBack()) {
                     webView.goBack()
                 } else {
                     // 没有上一页，关闭页面
                     isEnabled = false
                     onBackPressedDispatcher.onBackPressed()
                 }
             }
        }
         // 绑定到返回调度器
        onBackPressedDispatcher.addCallback(this, backCallback)
    }
    private fun initWebView() {
        val webSettings: WebSettings = webView.settings
        // 开启JS
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        // 支持DOM存储
        webSettings.domStorageEnabled = true
        // 自适应屏幕
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        // 缩放
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = false

        // 拦截页面跳转，不调用系统浏览器
        webView.webViewClient = object : WebViewClient() {}
        // 支持弹窗、标题获取
        webView.webChromeClient = WebChromeClient()

        // JS 调用原生方法
        webView.addJavascriptInterface(JsBridge(), "AndroidBridge")

        // 加载网页：远程链接 / 本地html
        webView.loadUrl("https://autumn-wildflower-2e2c.freshingair.dpdns.org/")
        // webView.loadUrl("file:///android_asset/index.html")
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                webView.loadUrl("file:///android_asset/index.html")
            }
        }
    }
    // JS交互桥接类
    @Keep inner class JsBridge {
        @Keep @JavascriptInterface fun yes() {
            runOnUiThread {
                val dlg = DialogUtils.getAlertDialog(this@BrowserActivity, true)
                    .setTitle("警告")
                    .setMessage("不要贸然去做这件事，别抱着侥幸心理试探底线。一时的冲动只会埋下巨大隐患，不仅会伤害他人，也会让自己付出沉重代价，酿成无法挽回的后果，凡事三思而行，切勿一意孤行。")
                    // 这里设置点击事件null，再重新写点击事件，屏蔽点击之后对话框消失
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("我知道我在干什么！") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(this@BrowserActivity, LoadingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                dlg.show()
                setAlertDialog(this@BrowserActivity, dlg, null)
                    // ContextCompat.getColor(this@BrowserActivity, R.color.black))
            }
        }
        @Keep @JavascriptInterface fun no(){
            this@BrowserActivity.finish()
        }
    }

    // 销毁防止内存泄漏
    override fun onDestroy() {
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        webView.destroy()
        super.onDestroy()
    }
}
