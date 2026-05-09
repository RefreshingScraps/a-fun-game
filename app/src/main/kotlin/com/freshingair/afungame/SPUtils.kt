package com.freshingair.afungame

import android.content.Context
import androidx.core.content.edit


object SPUtils {
    // 文件名（随便写，代表你的配置文件）
    private const val SP_NAME = "AppSettings"

    // 保存 布尔类型（如开关）
    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sp.edit { putBoolean(key, value) }
    }

    // 读取 布尔类型
    fun getBoolean(context: Context, key: String?, defaultValue: Boolean): Boolean {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(key, defaultValue)
    }

    // 清空所有设置
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sp.edit { clear() }
    }
}