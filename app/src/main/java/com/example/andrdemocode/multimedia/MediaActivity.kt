package com.example.andrdemocode.multimedia

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.display.DisplayManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.WindowInsets
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.example.andrdemocode.R
import com.example.andrdemocode.base.XLog
import com.example.andrdemocode.databinding.ActivityMediaBinding
import java.io.File

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var outputImage: File
    private lateinit var imageUri: Uri
    private lateinit var imageView: ImageView
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                takePictureLauncher.launch(null)
            } else {
                XLog.i("RequestPermission Fail")
            }
        }

    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sendNotice()
        takePhoto()
        mediaPlay()

        display()
    }

    /**
     * Display：表示物理显示设备，提供屏幕尺寸、密度、刷新率等信息
     */
    private fun display() {
        // 获取屏幕尺寸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            // 获取Display对象
            val display = display
            XLog.i(display.toString())

            val currentWindowMetrics = windowManager.currentWindowMetrics
            val rect = currentWindowMetrics.bounds
            XLog.i("屏幕宽高 ${rect.width()} ${rect.height()}")  // 1080 2340
            val insets =
                currentWindowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())  // 任何系统栏
            XLog.i("系统栏宽高 $insets")
            XLog.i("app实际宽高 ${rect.width() - insets.left - insets.right} ${rect.height() - insets.top - insets.bottom}")
        }
        val displays = (getSystemService(DISPLAY_SERVICE) as DisplayManager).displays
        XLog.i("获取多屏display ${displays.size}")

        val displayMetrics = resources.displayMetrics
        XLog.i("displayMetrics ${displayMetrics.widthPixels} ${displayMetrics.heightPixels}") // 1080 2197 不包括系统装饰

    }

    private fun mediaPlay() {
        initMediaPlay()
        initVideoPlay()
        binding.apply {
            playA.setOnClickListener {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
            pauseA.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
            stopA.setOnClickListener {
                mediaPlayer.reset()
                initMediaPlay()
            }
            playV.setOnClickListener {
                if (!videoView.isPlaying) {
                    videoView.start()
                }
            }
            pauseV.setOnClickListener {
                if (videoView.isPlaying) {
                    videoView.pause()
                }
            }
            replayV.setOnClickListener {
                videoView.resume()
            }
        }


    }

    private fun initMediaPlay() {
        val assetManager = assets
        val fd = assetManager.openFd("song.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer.prepare()
    }

    private fun initVideoPlay() {
        val uri = Uri.parse("android.resource://$packageName/${R.raw.movie}")
        binding.videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
    }

    private fun takePhoto() {
        imageView = findViewById(R.id.img_taken)
        findViewById<Button>(R.id.take_photo).setOnClickListener {
            outputImage = File(externalCacheDir, "output_img.jpg")  // 照片保存指定路径
            if (outputImage.exists()) outputImage.delete()
            outputImage.createNewFile()

            imageUri = FileProvider.getUriForFile(this, "com.example.andrdemocode.fileprovider", outputImage)

            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(takePhotoIntent, RQC)

//            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == RQC) {
            val bitmap = BitmapFactory.decodeFile(outputImage.absolutePath)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun sendNotice() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建通知渠道
            val channel =
                NotificationChannel(ID, "我的通知", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        findViewById<Button>(R.id.send_notice).setOnClickListener {
            // 创建通知
            val intent = Intent(this, MediaActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(this, ID)
                .setContentTitle("通知的标题")
                .setContentText("通知的一些内容")
                .setSmallIcon(R.drawable.btn_call_normal)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            handler.postDelayed({
                notificationManager.notify(1, notification)
                XLog.i("notify")
            }, 5000)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()

        binding.videoView.suspend()
    }

    companion object {
        private const val ID = "c_id"
        private const val RQC = 21
    }
}