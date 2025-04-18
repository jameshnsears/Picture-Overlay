package picture.overlay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import picture.overlay.theme.Theme
import picture.overlay.wip.composeable.OverlayPermissionScreen
import picture.overlay.wip.composeable.OverlaySettingsPicture

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Theme {
//                App()


                Scaffold { innerPadding ->
                    OverlaySettingsPicture()

                    OverlayPermissionScreen()
                }

                // TODO have a button on OverlayPermissionScreen to Display Overlay

                // TODO once permission is granted, use "Display Overlay" press to use navigation to move close OverlayPermissionScreen and display overlay
            }
        }
    }
}
