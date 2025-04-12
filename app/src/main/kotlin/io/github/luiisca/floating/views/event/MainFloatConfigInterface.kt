package io.github.luiisca.floating.views.event

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.View
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

data class MainFloatConfigInterface(
    val composable: (@Composable () -> Unit)? = null,
    val viewFactory: ((Context) -> View)? = null,
    override var startPointDp: PointF? = null,
    override var startPointPx: PointF? = null,
    override var draggingTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>) = {
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        )
    },
    override var snapToEdgeTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>) = {
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    },
    override var snapToCloseTransitionSpec: (Transition.Segment<Point>.() -> FiniteAnimationSpec<Int>) = {
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    },
    override var isSnapToEdgeEnabled: Boolean = true,
    override var onTap: ((Offset) -> Unit)? = null,
    override var onDragStart: ((offset: Offset) -> Unit)? = null,
    override var onDrag: ((
        change: PointerInputChange,
        dragAmount: Offset,
        newPoint: Point,
        newAnimatedPoint: Point?
    ) -> Unit)? = null,
    override var onDragEnd: (() -> Unit)? = null,
) : FloatConfigInterface
