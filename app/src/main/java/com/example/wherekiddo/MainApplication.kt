package com.example.wherekiddo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.example.wherekiddo.util.service.NotificationChannels.LOCATION_CHANNEL
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication: Application() {

    override fun onCreate() {

        super.onCreate()

        Timber.plant(Timber.DebugTree())

        buildLocationNotificationChannel()
    }

    private fun buildLocationNotificationChannel() {

        if (SDK_INT >= O) {

            val channel = NotificationChannel(LOCATION_CHANNEL, "Location", IMPORTANCE_LOW)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}