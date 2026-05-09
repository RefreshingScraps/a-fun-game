package com.freshingair.afungame

import android.content.Context
import android.util.Log
import com.freshingair.afungame.DialogUtils.setAlertDialog
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class AppCrashHandler : Thread.UncaughtExceptionHandler {
    private var context: Context? = null
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    fun init(ctx: Context) {
        context = ctx
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("AppCrashHandler", "thread ${t.name} error: ${e.message}")
        saveErrorInfo(e)

        val dlg = DialogUtils.getAlertDialog(context, true)
            .setTitle("App发生了未捕获的异常")
            .setMessage("错误信息：\n${e.message}")
            // 这里设置点击事件null，再重新写点击事件，屏蔽点击之后对话框消失
            .setNegativeButton("退出应用") { dialog, _ ->
                dialog.dismiss()
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            .setPositiveButton("继续运行应用") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
        dlg.show()
        context?.let { setAlertDialog(it, dlg, null) }
    }
    private fun saveErrorInfo(e: Throwable) {
        val writer = StringWriter()
        e.printStackTrace(PrintWriter(writer))
        val log = writer.toString()
        File(context?.filesDir, "crash.log").writeText(log)
    }
}