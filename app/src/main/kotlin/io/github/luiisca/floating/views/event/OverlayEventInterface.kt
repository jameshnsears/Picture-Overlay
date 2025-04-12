package io.github.luiisca.floating.views.event

import android.graphics.Point
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

interface OverlayEventInterface {
    var onTap: ((Offset) -> Unit)?
    var onDragStart: ((offset: Offset) -> Unit)?
    var onDrag: ((
        change: PointerInputChange,
        dragAmount: Offset,
        newPoint: Point,
        newAnimatedPoint: Point?
    ) -> Unit)?
    var onDragEnd: (() -> Unit)?
}
