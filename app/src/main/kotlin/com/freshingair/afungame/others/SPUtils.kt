package com.freshingair.afungame.others

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import java.io.InputStreamReader

object SPUtils {
    // 文件名（随便写，代表你的配置文件）
    private const val SP_NAME = "AppSettings"
    // 保存 布尔类型（如开关）
    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sp.edit { putBoolean(key, value) }
    }
    // 读取 布尔类型
    fun getBoolean(context: Context, key: String?, defaultValue: Boolean = true): Boolean {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(key, defaultValue)
    }
    // 清空所有设置
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sp.edit { clear() }
    }

    data class UpdateLog(val latestVersion: LatestVersion)
    data class LatestVersion(val versionCode: Int, val versionName: String, val updateTime: String, val updateType: String)
    // 从 assets 读取 JSON
    fun readVersionNameFromAssets(context: Context): String {
        val inputStream = context.assets.open("version/update-catalog.json")
        val reader = InputStreamReader(inputStream)
        val updateLog = Gson().fromJson(reader, UpdateLog::class.java)
        return updateLog.latestVersion.versionName
    }
}