package picture.overlay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import picture.overlay.theme.Theme
import picture.overlay.wip.OverlayPermissionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Theme {
//                App()


                Scaffold { innerPadding ->
                    OverlayPermissionScreen()
                }
            }
        }
    }
}
