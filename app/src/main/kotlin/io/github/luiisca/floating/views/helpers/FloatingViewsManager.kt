package io.github.luiisca.floating.views.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat
import picture.overlay.R
import io.github.luiisca.floating.views.FloatingViewsConfig
import io.github.luiisca.floating.views.FloatingViewsService
import androidx.core.net.toUri

object FloatingViewsManager {
    var notificationIcon: Int = R.drawable.round_bubble_chart_24
    var notificationTitle: String = "Floating views running"

    fun setNotificationProperties(icon: Int? = null, title: String? = null) {
        if (icon != null) {
            notificationIcon = icon
        }
        if (title != null) {
            notificationTitle = title
        }
    }

    /**
     * Starts the floating service if the overlay permission is granted, or requests the permission otherwise.
     *
     * @param context The service context
     * @param config The config [FloatingViewsConfig] to use for this dynamic floating view
     * @param serviceClass The class of the service to be started.
     */
    fun startFloatServiceIfPermitted(
        context: Context,
        config: FloatingViewsConfig,
        serviceClass: Class<*> = FloatingViewsService::class.java
    ) {
        if (canDrawOverlays(context)) {
            val intent = Intent(context, serviceClass).apply {
                putExtra("CONFIG_ID", ConfigManager.addConfig(config))
            }
            ContextCompat.startForegroundService(context, intent)
        } else {
            requestOverlayPermission(context)
        }
    }

    /**
     * Stops the floating service if running.
     *
     * @param context The service context
     * @param serviceClass The class of the service to be stopped.
     */
    fun stopFloatService(
        context: Context,
        serviceClass: Class<*> = FloatingViewsService::class.java
    ) {
        context.stopService(Intent(context, serviceClass))
    }

    private fun canDrawOverlays(context: Context) = Settings.canDrawOverlays(context)

    private fun requestOverlayPermission(context: Context) {
        @SuppressLint("InlinedApi")
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        context.startActivity(intent)
    }
}