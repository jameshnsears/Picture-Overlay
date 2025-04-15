package io.github.luiisca.floating.views.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.github.luiisca.floating.views.OverlayManager
import io.github.luiisca.floating.views.helpers.ConfigHelper

class OverlayService : Service() {
    private lateinit var overlayManager: OverlayManager
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()

        overlayManager = OverlayManager(
            this,
            stopService = { stopSelf() },
        )

        // elevate service to foreground status to make it less likely to be terminated by the system under memory pressure
        overlayManager.startForegroundService()
        OverlayServiceState.setServiceRunning(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val configId = intent?.getStringExtra("CONFIG_ID") ?: return START_NOT_STICKY
        val config = ConfigHelper.getConfig(configId) ?: return START_NOT_STICKY
        // Creates and starts a new dynamic, interactive floating view.
        overlayManager.startDynamicFloatingView(config)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        overlayManager.stopAllDynamicFloatingViews()
        OverlayServiceState.setServiceRunning(false)
    }
}