package com.freshingair.afungame.others

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.ImagesPlugin
import java.io.BufferedReader
import java.io.InputStreamReader

object DialogUtils {
    fun getAlertDialog(context: Context?, isLight: Boolean): AlertDialog.Builder {
        return AlertDialog.Builder(
            ContextThemeWrapper(context, getDialogTheme(isLight))
        )
    }
    @Suppress("DEPRECATION")
    private fun getDialogTheme(isLight: Boolean): Int {
        return (if (isLight)
            R.style.Theme_Holo_Light_Dialog
        else
            R.style.Theme_Holo_Dialog)
    }
    @SuppressLint("DiscouragedApi")
    fun setAlertDialog(context: Context, dlg: AlertDialog){ // messageColor: Int?, btnColor: Int?)
        val alertTitleId = context.resources.getIdentifier("alertTitle", "id", "android")
        val title: TextView = dlg.findViewById(alertTitleId)
        title.setTextColor(ContextCompat.getColor(context,R.color.holo_blue_bright))
    }
    /**
     * 从 assets 读取 md 文件并显示在 Dialog
     */
    fun showMarkdownDialog(context: Context, assetFileName: String, title: String = "说明", listener: DialogInterface.OnClickListener) {
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
            .usePlugin(StrikethroughPlugin.create())    // 删除线
            .usePlugin(TaskListPlugin.create(context))  // 任务列表
            .usePlugin(ImagesPlugin.create())           // 图片
            .build()

        // 4. 渲染MD到TextView
        markwon.setMarkdown(textView, content)

        val dlg = getAlertDialog(context, true)
            .setTitle(title)
            .setView(scrollView)
            .setPositiveButton("关闭", listener)
            .setCancelable(false)
            .create()
        dlg.show()
        setAlertDialog(context, dlg)
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