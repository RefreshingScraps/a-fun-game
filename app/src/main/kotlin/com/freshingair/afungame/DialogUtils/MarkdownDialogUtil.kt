package com.freshingair.afungame.DialogUtils

import android.content.Context
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.table.TablePlugin
import io.noties.markwon.linkify.LinkifyPlugin
import java.io.BufferedReader
import java.io.InputStreamReader

object MarkdownDialogUtil {

    /**
     * 从 assets 读取 md 文件并显示在 Dialog
     */
    fun showMarkdownDialog(context: Context, assetFileName: String, title: String = "说明") {
        // 1. 读取文件内容
        val content = readMarkdownFromAssets(context, assetFileName)

        // 2. 创建可滚动的TextView
        val scrollView = ScrollView(context)
        val textView = TextView(context).apply {
            setPadding(50, 30, 50, 30)
            textSize = 14f
        }
        scrollView.addView(textView)

        // 3. 配置 Markwon 渲染器（支持表格、删除线、链接等）
        val markwon = Markwon.builder(context)
            .usePlugin(TablePlugin.create())
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .build()

        // 4. 渲染MD到TextView
        markwon.setMarkdown(textView, content)

        // 5. 构建并显示Dialog
        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(scrollView)
            .setPositiveButton("关闭", null)
            .show()
    }

    /**
     * 读取 assets 中的 .md 文件
     */
    private fun readMarkdownFromAssets(context: Context, fileName: String): String {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.readText()
            reader.close()
            inputStream.close()
            content
        } catch (e: Exception) {
            "读取文件失败：${e.message}"
        }
    }
}