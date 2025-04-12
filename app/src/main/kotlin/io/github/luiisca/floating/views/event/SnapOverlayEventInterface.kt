package io.github.luiisca.floating.views.event

import android.graphics.Point
import android.graphics.PointF
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition

interface SnapOverlayEventInterface : OverlayEventInterface {
    var startPointDp: PointF?
    var startPointPx: PointF?
    var draggingTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>)
    var snapToEdgeTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>)
    var snapToCloseTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>)
    var isSnapToEdgeEnabled: Boolean
}
