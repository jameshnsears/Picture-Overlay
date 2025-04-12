package io.github.luiisca.floating.views.data

import io.github.luiisca.floating.views.event.ExpandedFloatConfigInterface
import io.github.luiisca.floating.views.event.MainFloatConfigInterface

data class FloatingViewsConfig(
    val enableAnimations: Boolean = true,
    val main: MainFloatConfigInterface,
    val close: CloseFloatConfigData,
    val expanded: ExpandedFloatConfigInterface
)
