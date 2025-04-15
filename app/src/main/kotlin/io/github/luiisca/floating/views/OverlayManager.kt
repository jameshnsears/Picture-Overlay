package io.github.luiisca.floating.views

import android.app.Service
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.ServiceCompat
import io.github.luiisca.floating.views.data.OverlayConfigData
import io.github.luiisca.floating.views.helpers.OverlayHelper
import io.github.luiisca.floating.views.helpers.NotificationHelper

enum class CloseBehavior {
    MAIN_SNAPS_TO_CLOSE_FLOAT,
    CLOSE_SNAPS_TO_MAIN_FLOAT,
}

class OverlayManager(
    private val context: Context,
    private val stopService: () -> Unit,
) {
    private val composeOwner = OverlayViewStorage()
    private var isComposeOwnerInit: Boolean = false
    private val windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
    private var floatsCount: Int = 0
    private val addedViews = mutableListOf<View>()

    fun startForegroundService(icon: Int? = null, title: String? = null) {
        val service = context as? Service
            ?: throw IllegalStateException("This function must be called from a Service context")

        val notificationHelper = NotificationHelper(service)
        notificationHelper.createNotificationChannel()
        val notificationIcon: Int = icon ?: OverlayHelper.notificationIcon
        val notificationTitle: String = title ?: OverlayHelper.notificationTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                service,
                notificationHelper.notificationId,
                notificationHelper.createDefaultNotification(notificationIcon, notificationTitle),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            )
        } else {
            service.startForeground(
                notificationHelper.notificationId,
                notificationHelper.createDefaultNotification(notificationIcon, notificationTitle)
            )
        }
    }

    fun startDynamicFloatingView(config: OverlayConfigData) {
        floatsCount += 1

        OverlayFactory(
            context,
            config,
            getFloatsCount = { floatsCount },
            setFloatsCount = { floatsCount = it },
            stopService,
            addViewToTrackingList = { view -> addedViews.add(view) },
            composeOwner,
            getIsComposeOwnerInit = { isComposeOwnerInit },
            setIsComposeOwnerInit = { isComposeOwnerInit = it }
        )
    }

    fun stopAllDynamicFloatingViews() {
        addedViews.forEach { view ->
            try {
                windowManager.removeView(view)
            } catch (_: IllegalArgumentException) {
                Log.e("error", "could not remove view")
            }
        }
        addedViews.clear()
    }
}
