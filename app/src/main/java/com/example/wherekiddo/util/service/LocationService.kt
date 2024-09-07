package com.example.wherekiddo.util.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.wherekiddo.R
import com.example.wherekiddo.repository.interactors.TrackingInteractor
import com.example.wherekiddo.ui.util.IntentExtras.VEHICLE_PLATES
import com.example.wherekiddo.util.extensions.toFormattedString
import com.example.wherekiddo.util.location.DefaultLocationClient
import com.example.wherekiddo.util.location.LocationClient
import com.example.wherekiddo.util.service.NotificationChannels.LOCATION_CHANNEL
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

private const val SERVICE_TITLE = "Tracking location..."
private const val SERVICE_ID = 1

@AndroidEntryPoint
class LocationService: Service() {

    @Inject
    lateinit var trackingRepository: TrackingInteractor

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {

            ACTION_START -> start(intent.getStringExtra(VEHICLE_PLATES))
            ACTION_STOP  -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(vehiclePlates: String?) {

        vehiclePlates?.let {

            val notification = NotificationCompat.Builder(this, LOCATION_CHANNEL)
                .setContentTitle(SERVICE_TITLE)
                .setContentText("Location unknown")
                .setSmallIcon(R.drawable.location_icon)
                .setOngoing(true)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            locationClient
                .getLocationUpdates(5000L)
                .catch { e ->

                    e.printStackTrace()
                    Timber.i("Exception - ${e.message}")
                }
                .onEach { location ->

                    val updatedNotification = notification.setContentText(location.toFormattedString())

                    notificationManager.notify(SERVICE_ID, updatedNotification.build())

                    trackingRepository.updateVehicleLocation(
                        vehiclePlates = it,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        onComplete = { isSuccessful ->

                            if (isSuccessful) {

                                Timber.i("Location: Sent location ${location.toFormattedString()}")

                            } else {

                                Timber.i("Location: Location update failed!")
                            }
                        }
                    )
                }
                .launchIn(serviceScope)

            startForeground(SERVICE_ID, notification.build())
        }

    }

    private fun stop() {

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }

    companion object {

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}