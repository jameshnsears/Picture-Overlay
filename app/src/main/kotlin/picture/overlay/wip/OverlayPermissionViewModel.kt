package picture.overlay.wip

import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OverlayPermissionViewModel : ViewModel() {
    private val _overlayPermissionGranted = mutableStateOf(false)
    val overlayPermissionGranted: State<Boolean> = _overlayPermissionGranted

    fun checkOverlayPermission(context: Context) {
        _overlayPermissionGranted.value = Settings.canDrawOverlays(context)
    }

    fun startServiceOverlayControl(context: Context) {
        /*
        OverlayControl
         */
    }
}
