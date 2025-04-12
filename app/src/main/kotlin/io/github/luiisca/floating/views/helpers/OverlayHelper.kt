package io.github.luiisca.floating.views.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import io.github.luiisca.floating.views.data.OverlayConfigData
import io.github.luiisca.floating.views.service.OverlayService
import picture.overlay.R

object OverlayHelper {
    var notificationIcon: Int = R.drawable.picure_overlay_24
    var notificationTitle: String = "Picture Overlay is running"

    fun startFloatServiceIfPermitted(
        context: Context,
        config: OverlayConfigData,
        serviceClass: Class<*> = OverlayService::class.java
    ) {
        if (canDrawOverlays(context)) {
            val intent = Intent(context, serviceClass).apply {
                putExtra("CONFIG_ID", ConfigHelper.addConfig(config))
            }
            ContextCompat.startForegroundService(context, intent)
        } else {
            requestOverlayPermission(context)
        }
    }

    fun stopFloatService(
        context: Context,
        serviceClass: Class<*> = OverlayService::class.java
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