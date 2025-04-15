package picture.overlay.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.luiisca.floating.views.data.CloseOverlayData
import io.github.luiisca.floating.views.event.ExpandedOverlayEventInterface
import io.github.luiisca.floating.views.data.OverlayConfigData
import io.github.luiisca.floating.views.event.ActiveOverlayEventInterface
import io.github.luiisca.floating.views.service.OverlayServiceState
import io.github.luiisca.floating.views.helpers.OverlayHelper
import picture.overlay.composable.stopwatch.StopwatchCloseComposable
import picture.overlay.composable.stopwatch.StopwatchComposable

@Preview
@Composable
fun App() {
    Scaffold { innerPadding ->
        println(innerPadding)

        val context = LocalContext.current
        val isServiceRunning by OverlayServiceState.isServiceRunning.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
                onClick = {
                    val config = OverlayConfigData(
                        main = ActiveOverlayEventInterface(
                            composable = { StopwatchComposable() },
                            // Add other main float configurations here
                        ),

                        // TODO JS - Expanded is what happens when you click on the stopwatch
                        expanded = ExpandedOverlayEventInterface(
                            enabled = false,
                        )
                        /*
                        val expandedFloatConfig = ExpandedFloatConfig(
                            enabled = true,
                            tapOutsideToClose = true,
                            dimAmount = 0.5f,
                            composable = { close -> /* Expanded content */ }
                        )
                         */,

                        close = CloseOverlayData(
                            composable = { StopwatchCloseComposable() },
                            enabled = true,
                        ),
                        /*
                        val closeFloatConfig = CloseFloatConfig(
                            enabled = true,
                            composable = { /* Custom close button */ },
                            closeBehavior = CloseBehavior.MAIN_SNAPS_TO_CLOSE_FLOAT
                        )
                         */

                        )

                    // Launch a new stopwatch floating view
                    OverlayHelper.startFloatServiceIfPermitted(context, config)
                }
            ) {
                Text(text = "Stopwatch", style = MaterialTheme.typography.bodyLarge)
            }

            if (isServiceRunning) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
                    onClick = {
                        OverlayHelper.stopFloatService(context)
                    }
                ) {
                    Text(text = "Remove all", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}