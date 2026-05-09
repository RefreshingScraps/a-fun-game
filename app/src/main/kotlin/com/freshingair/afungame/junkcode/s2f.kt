package com.freshingair.afungame.junkcode

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.IOException


object s2f {
    /**
     * 将字符串内容 写入文件，并返回 File 对象
     * @param content 要写入的字符串
     * @return 生成的文件
     */
    fun stringToFile(context: Context, content: String): File? {
        // 1. 创建文件（存在则覆盖，不存在则新建）
        val file = File(context.filesDir, "demo.txt")

        try {
            // 2. 写入字符串
            val writer = FileWriter(file)
            writer.write(content)
            writer.close()

            return file // 返回最终的 File
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}