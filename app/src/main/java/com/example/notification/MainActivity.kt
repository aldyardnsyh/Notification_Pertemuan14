package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var channelId = "TEST_NOTIF"
    private val notifId = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.btnUpdate.setOnClickListener {
            val notifImage = BitmapFactory.decodeResource(resources, R.drawable.active)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.active)
                .setContentTitle("Update Notification")
                .setContentText("New Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(notifImage))

            notificationManager.notify(notifId, builder.build())
        }

        binding.btnNotif.setOnClickListener {

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_MUTABLE
            } else {
                0
            }
            val intent = Intent(this, NotifReceiver::class.java).putExtra("MESSAGE", "This is a test notification")
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flag)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Test Notification")
                .setContentText("This is a test notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.baseline_notifications_active_24, "Open", pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(
                    channelId,
                    "Test Notification",
                    NotificationManager.IMPORTANCE_DEFAULT)
                with(notificationManager) {
                    createNotificationChannel(notifChannel)
                    notify(notifId, builder.build())
                }
            }
            else {
                    notificationManager.notify(notifId, builder.build())
                }
            }
        }
    }