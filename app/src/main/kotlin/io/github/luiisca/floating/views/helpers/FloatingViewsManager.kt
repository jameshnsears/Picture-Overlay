package io.github.luiisca.floating.views.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import io.github.luiisca.floating.views.data.FloatingViewsConfig
import io.github.luiisca.floating.views.service.FloatingViewsService
import picture.overlay.R

object FloatingViewsManager {
    var notificationIcon: Int = R.drawable.picure_overlay_24
    var notificationTitle: String = "Picture Overlay is running"

    fun setNotificationProperties(icon: Int? = null, title: String? = null) {
        if (icon != null) {
            notificationIcon = icon
        }
        if (title != null) {
            notificationTitle = title
        }
    }

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