package com.example.notekeeper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Reading reminders"

            val nm =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nm.createNotificationChannel(channel)
        }
    }
}