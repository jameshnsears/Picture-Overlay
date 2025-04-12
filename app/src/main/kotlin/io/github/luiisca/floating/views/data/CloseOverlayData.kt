package io.github.luiisca.floating.views.data

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.View
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import io.github.luiisca.floating.views.CloseBehavior

data class CloseOverlayData(
    val enabled: Boolean = true,
    val composable: (@Composable () -> Unit)? = null,
    val viewFactory: ((Context) -> View)? = null,
    var startPointDp: PointF? = null,
    var startPointPx: PointF? = null,
    val mountThresholdDp: Float? = null,
    val mountThresholdPx: Float? = null,
    val closingThresholdDp: Float? = null,
    val closingThresholdPx: Float? = null,
    val bottomPaddingDp: Float? = null,
    val bottomPaddingPx: Float? = null,
    var draggingTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>) = {
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    },
    var snapToMainTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>) = {
        spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    },
    var closeBehavior: CloseBehavior? = CloseBehavior.MAIN_SNAPS_TO_CLOSE_FLOAT,
    var followRate: Float = 0.1f,
)
