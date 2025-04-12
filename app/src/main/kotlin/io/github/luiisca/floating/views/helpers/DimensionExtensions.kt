package io.github.luiisca.floating.views.helpers

import android.content.res.Resources

internal fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)
