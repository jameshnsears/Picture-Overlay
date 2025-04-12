package io.github.luiisca.floating.views.data

import io.github.luiisca.floating.views.event.ExpandedOverlayEventInterface
import io.github.luiisca.floating.views.event.ActiveOverlayEventInterface

data class OverlayConfigData(
    val enableAnimations: Boolean = true,
    val main: ActiveOverlayEventInterface,
    val close: CloseOverlayData,
    val expanded: ExpandedOverlayEventInterface
)
