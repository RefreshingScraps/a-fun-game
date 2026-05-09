package com.freshingair.afungame

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.annotation.MainThread
import kotlinx.coroutines.*

class FadeImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {
    private val viewScope = CoroutineScope(Dispatchers.Main + Job())

    var assetsPathList: MutableList<String> = mutableListOf()
    var fadeInDuration: Long = 800L
    var stayDuration: Long = 3000L
    var fadeOutDuration: Long = 800L
    var isLoop: Boolean = true

    private var currentIndex = 0
    private var isRunning = false

    // 统一回调接口
    interface FadeListener {
        fun onFadeInStart(path: String, index: Int) {}
        fun onFadeInEnd(path: String, index: Int) {}
        fun onFadeOutStart(path: String, index: Int) {}
        fun onFadeOutEnd(path: String, index: Int) {}
        fun onPageChange(nextIndex: Int) {}
        fun onPlayAllEnd() {}
    }

    var fadeListener: FadeListener? = null

    @MainThread
    fun setImagePaths(
        paths: List<String>,
        fadeInMs: Long = 800L,
        stayMs: Long = 3000L,
        fadeOutMs: Long = 800L,
        loop: Boolean = true
    ) {
        stopSlide()
        assetsPathList.clear()
        assetsPathList.addAll(paths)
        fadeInDuration = fadeInMs
        stayDuration = stayMs
        fadeOutDuration = fadeOutMs
        isLoop = loop
        currentIndex = 0
        startSlide()
    }

    fun startSlide() {
        if (isRunning || assetsPathList.isEmpty()) return
        isRunning = true
        runSlide()
    }

    private fun runSlide() {
        viewScope.launch {
            while (isRunning) {
                val path = assetsPathList[currentIndex]
                val bitmap = withContext(Dispatchers.IO) {
                    runCatching {
                        context.assets.open(path).use { BitmapFactory.decodeStream(it) }
                    }.getOrNull()
                }

                bitmap?.let {
                    alpha = 0f
                    setImageBitmap(it)

                    fadeListener?.onFadeInStart(path, currentIndex)
                    animate().alpha(1f).setDuration(fadeInDuration).withLayer().start()
                    delay(fadeInDuration)
                    fadeListener?.onFadeInEnd(path, currentIndex)

                    delay(stayDuration)

                    fadeListener?.onFadeOutStart(path, currentIndex)
                    animate().alpha(0f).setDuration(fadeOutDuration).withLayer().start()
                    delay(fadeOutDuration)
                    fadeListener?.onFadeOutEnd(path, currentIndex)
                }

                currentIndex++
                if (currentIndex >= assetsPathList.size) {
                    if (isLoop) {
                        currentIndex = 0
                    } else {
                        fadeListener?.onPlayAllEnd()
                        break
                    }
                }
                fadeListener?.onPageChange(currentIndex)
            }
            isRunning = false
        }
    }

    fun stopSlide() {
        isRunning = false
        animate().cancel()
        viewScope.coroutineContext.cancelChildren()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopSlide()
    }
}
