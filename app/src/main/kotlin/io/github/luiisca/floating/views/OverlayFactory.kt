package io.github.luiisca.floating.views

import android.app.Service
import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import io.github.luiisca.floating.views.composable.CloseFloat
import io.github.luiisca.floating.views.composable.DefaultCloseButton
import io.github.luiisca.floating.views.composable.DraggableFloat
import io.github.luiisca.floating.views.composable.FullscreenOverlayFloat
import io.github.luiisca.floating.views.data.OverlayConfigData

internal fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

class OverlayFactory(
    private val context: Context,
    private val config: OverlayConfigData,
    private val getFloatsCount: () -> Int,
    private val setFloatsCount: (newCount: Int) -> Unit,
    private val stopService: () -> Unit,
    private val addViewToTrackingList: (view: View) -> Unit,
    private val composeOwner: OverlayViewStorage,
    private val getIsComposeOwnerInit: () -> Boolean,
    private val setIsComposeOwnerInit: (bool: Boolean) -> Unit,
) {
    private val windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    private var mainView: ComposeView? = null
    private var expandedView: ComposeView? = null
    private var overlayView: ComposeView? = null
    private var closeView: ComposeView? = null
    private val mainStartPoint = Point(
        (config.main.startPointDp?.x?.toPx() ?: config.main.startPointPx?.x ?: 0f).toInt(),
        (config.main.startPointDp?.y?.toPx() ?: config.main.startPointPx?.y ?: 0f).toInt()
    )
    private val expandedStartPoint = Point(
        (config.expanded.startPointDp?.x?.toPx() ?: config.expanded.startPointPx?.x ?: 0f).toInt(),
        (config.expanded.startPointDp?.y?.toPx() ?: config.expanded.startPointPx?.y ?: 0f).toInt()
    )
    private var mainLayoutParams = baseLayoutParams().apply {
        flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        x = mainStartPoint.x
        y = mainStartPoint.y
    }
    private var expandedLayoutParams = baseLayoutParams().apply {
        flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dimAmount = config.expanded.dimAmount
        x = expandedStartPoint.x
        y = expandedStartPoint.y
    }
    private var closeLayoutParams = baseLayoutParams().apply {
        flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
    }
    private val overlayLayoutParams = baseLayoutParams().apply {
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
    }

    init {
        if (config.close.enabled) {
            createCloseView()
        }

        createMainView()
    }

    private fun createMainView() {
        val _mainView = ComposeView(context).apply {
            mainView = this

            this.setContent {
                DraggableFloat(
                    windowManager = windowManager,
                    containerView = mainView!!,
                    closeView = closeView!!,
                    layoutParams = mainLayoutParams,
                    closeLayoutParams = closeLayoutParams,
                    config = config,
                    updateSize = { size ->
                        updateSize(this, mainLayoutParams, size)
                    },
                    onKey = { event ->
                        if (event.key == Key.Back) {
                            tryCloseDraggable()

                            true
                        } else {
                            false
                        }
                    },
                    onDestroy = {
                        tryCloseDraggable(true)
                        if (getFloatsCount() <= 1) {
                            stopService()
                        }
                        setFloatsCount(getFloatsCount() - 1)
                    },
                    onTap = { offset ->
                        if (config.expanded.enabled) {
                            openExpanded()
                        }
                        config.main.onTap?.let { it(offset) }
                    },
                    onDragStart = config.main.onDragStart,
                    onDrag = config.main.onDrag,
                    onDragEnd = config.main.onDragEnd,
                ) {
                    when {
                        config.main.viewFactory != null -> config.main.viewFactory.let { viewFactory ->
                            AndroidView(
                                factory = { context ->
                                    viewFactory(context)
                                }
                            )
                        }

                        config.main.composable != null -> config.main.composable.invoke()
                        else -> throw IllegalArgumentException("Either compose or view must be provided for MainFloat")
                    }
                }
            }
        }

        _mainView.visibility = View.INVISIBLE
        compose(_mainView, mainLayoutParams)
        if (closeView != null) {
            tryRemoveView(closeView!!)
            compose(closeView!!, closeLayoutParams)
        }
    }

    private fun createExpandedView() {
        val _expandedView = ComposeView(context).apply {
            expandedView = this
            this.setContent {
                DraggableFloat(
                    windowManager = windowManager,
                    containerView = expandedView!!,
                    closeView = closeView!!,
                    closeLayoutParams = closeLayoutParams,
                    layoutParams = expandedLayoutParams,
                    config = config,
                    updateSize = { size ->
                        updateSize(this, expandedLayoutParams, size)
                    },
                    onKey = { event ->
                        if (event.key == Key.Back) {
                            tryCloseDraggable()

                            true
                        } else {
                            false
                        }
                    },
                    onDestroy = {
                        tryCloseDraggable(true)
                        if (getFloatsCount() <= 1) {
                            stopService()
                        }
                        setFloatsCount(getFloatsCount() - 1)
                    },
                    onTap = { offset ->
                        config.expanded.onTap?.let { it(offset) }
                    },
                    onDragStart = config.expanded.onDragStart,
                    onDrag = config.expanded.onDrag,
                    onDragEnd = config.expanded.onDragEnd,
                ) {
                    when {
                        config.expanded.viewFactory != null -> config.expanded.viewFactory.let { viewFactory ->
                            AndroidView(
                                factory = { context ->
                                    viewFactory(context) { tryCloseDraggable() }
                                }
                            )
                        }

                        config.expanded.composable != null -> config.expanded.composable.let { composable ->
                            composable { tryCloseDraggable() }
                        }

                        else -> throw IllegalArgumentException("Either compose or view must be provided for MainFloat")
                    }
                }
            }
        }

        _expandedView.visibility = View.INVISIBLE
        compose(_expandedView, expandedLayoutParams)
        if (closeView != null) {
            tryRemoveView(closeView!!)
            compose(closeView!!, closeLayoutParams)
        }
    }

    private fun createCloseView() {
        val _closeView = ComposeView(context).apply {
            closeView = this
            this.setContent {
                CloseFloat(updateSize = { size ->
                    windowManager.updateViewLayout(this, layoutParams.apply {
                        width = size.width
                        height = size.height
                    })
                }) {
                    when {
                        config.close.viewFactory != null -> config.close.viewFactory.let { factory ->
                            AndroidView(
                                factory = { context ->
                                    factory(context)
                                }
                            )
                        }

                        config.close.composable != null -> config.close.composable.invoke()
                        else -> DefaultCloseButton()
                    }
                }
            }
        }

        _closeView.visibility = View.INVISIBLE
    }

    private fun createOverlayView() {
        val _overlayView = ComposeView(context).apply {
            overlayView = this
            this.setContent {
                FullscreenOverlayFloat(
                    onTap = { tryCloseDraggable() }
                )
            }
        }

        compose(_overlayView, overlayLayoutParams)
    }

    private fun openExpanded() {
        tryRemoveView(mainView)

        if (config.expanded.tapOutsideToClose) {
            createOverlayView()
        }
        createExpandedView()
    }

    private fun tryCloseDraggable(destroy: Boolean = false) {
        tryRemoveView(mainView)
        tryRemoveView(expandedView)
        tryRemoveView(overlayView)

        if (!destroy) {
            createMainView()
        }
    }

    private fun tryRemoveView(view: ComposeView?) {
        try {
            windowManager.removeView(view)
        } catch (_: IllegalArgumentException) {
            Log.e("error", "could not remove view")
        }
    }

    private fun updateSize(
        composeView: ComposeView,
        layoutParams: WindowManager.LayoutParams,
        size: IntSize
    ) {
        windowManager.updateViewLayout(composeView, layoutParams.apply {
            width = size.width
            height = size.height
        })
        composeView.visibility = View.VISIBLE
    }

    private fun compose(composeView: ComposeView, layoutParams: WindowManager.LayoutParams) {
        composeView.consumeWindowInsets = false
        addToComposeLifecycle(composeView)
        windowManager.addView(composeView, layoutParams)
        addViewToTrackingList(composeView)
    }

    private fun addToComposeLifecycle(composable: ComposeView) {
        composeOwner.attachToDecorView(composable)
        if (!getIsComposeOwnerInit()) {
            composeOwner.onCreate()

            setIsComposeOwnerInit(true)
        }
        composeOwner.onStart()
        composeOwner.onResume()
    }

    private fun baseLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            gravity = Gravity.TOP or Gravity.START
            format = PixelFormat.TRANSLUCENT

            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
    }
}