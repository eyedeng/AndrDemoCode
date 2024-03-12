package com.example.andrdemocode.storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.andrdemocode.databinding.ActivityStorageBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer
import java.lang.StringBuilder

/**
 * Java I/O 顶层抽象父类：
 * InputStream 输入流，可read byte
 * OutputStream 输出流，可write byte
 * Reader 字符流 可read character
 * Writer 字符流 可write character
 * 子类使用装饰者模式，附加功能
 */
class StorageActivity : AppCompatActivity() {

    lateinit var binding: ActivityStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        load()
    }

    override fun onDestroy() {
        super.onDestroy()
        save(binding.edit.text.toString())
    }

    private fun load() {
        val sb = StringBuilder()
        try {
            val inputStream = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use {
                reader.forEachLine {
                    sb.append(it)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (sb.isNotEmpty()) {
            binding.edit.setText(sb)
        }
    }

    private fun save(data: String) {
        val outputStream = openFileOutput("data", MODE_PRIVATE)  // 输出流 字节写入
        val writer = BufferedWriter(OutputStreamWriter(outputStream))  // 带缓冲区的字符写入
        writer.use {
            it.write(data)
        }
    }



}