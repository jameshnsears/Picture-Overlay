package picture.overlay.wip

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.net.toUri


@Composable
fun OverlayPermissionScreen(
    viewModel: OverlayPermissionViewModel = viewModel()
) {
    val context = LocalContext.current

    val permissionGranted = viewModel.overlayPermissionGranted.value

    // check if permission already granted
    LaunchedEffect(Unit) {
        viewModel.checkOverlayPermission(context)
    }

    // request permission if not granted
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.checkOverlayPermission(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:${context.packageName}".toUri()
                )
                launcher.launch(intent)
            } else {
                viewModel.checkOverlayPermission(context)
            }
        },
            enabled = !permissionGranted
        ) {
            Text("Request Overlay Permission...")
        }

        Text(
            text = if (permissionGranted) "Overlay permission is granted ✅"
            else "Overlay permission is NOT granted ❌",
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            enabled = permissionGranted
        ) {
            Text("Display Overlay Control...")
        }
    }
}
