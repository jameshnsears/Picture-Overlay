package io.github.luiisca.floating.views.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import picture.overlay.R

@Preview
@Composable
fun DefaultCloseButton() {
    val iconColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    Box(
        modifier = Modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.rounded_cancel_24),
            contentDescription = "Close float view",
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.size(60.dp)
        )
    }
}
