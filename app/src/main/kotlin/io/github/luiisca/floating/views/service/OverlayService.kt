package io.github.luiisca.floating.views.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.github.luiisca.floating.views.OverlayController
import io.github.luiisca.floating.views.helpers.ConfigHelper

class OverlayService : Service() {
    private lateinit var overlayController: OverlayController
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()

        overlayController = OverlayController(
            this,
            stopService = { stopSelf() },
        )

        // elevate service to foreground status to make it less likely to be terminated by the system under memory pressure
        overlayController.initializeAsForegroundService()
        OverlayServiceStateHelper.setServiceRunning(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val configId = intent?.getStringExtra("CONFIG_ID") ?: return START_NOT_STICKY
        val config = ConfigHelper.getConfig(configId) ?: return START_NOT_STICKY
        // Creates and starts a new dynamic, interactive floating view.
        overlayController.startDynamicFloatingView(config)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        overlayController.stopAllDynamicFloatingViews()
        OverlayServiceStateHelper.setServiceRunning(false)
    }
}