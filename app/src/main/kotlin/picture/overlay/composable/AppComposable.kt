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
import picture.overlay.composable.stopwatch.StopwatchCloseFloat
import picture.overlay.composable.stopwatch.StopwatchFloat
import io.github.luiisca.floating.views.CloseFloatConfig
import io.github.luiisca.floating.views.ExpandedFloatConfig
import io.github.luiisca.floating.views.FloatingViewsConfig
import io.github.luiisca.floating.views.MainFloatConfig
import io.github.luiisca.floating.views.helpers.FloatServiceStateManager
import io.github.luiisca.floating.views.helpers.FloatingViewsManager

@Preview
@Composable
fun App() {
    Scaffold { innerPadding ->
        println(innerPadding)

        val context = LocalContext.current
        val isServiceRunning by FloatServiceStateManager.isServiceRunning.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stopwatch Float
            Button(
                modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
                onClick = {
                    val config = FloatingViewsConfig(
                        main = MainFloatConfig(
                            composable = { StopwatchFloat() },
                            // Add other main float configurations here
                        ),
                        close = CloseFloatConfig(
                            composable = { StopwatchCloseFloat() },
                            // Add other close float configurations here
                        ),
                        expanded = ExpandedFloatConfig(
                            enabled = false,
                            // Add other expanded float configurations here
                        )
                    )

                    // Launch a new stopwatch floating view
                    FloatingViewsManager.startFloatServiceIfPermitted(context, config)
                }
            ) {
                Text(text = "Stopwatch", style = MaterialTheme.typography.bodyLarge)
            }

            // Display a button to stop the service if it's running
            if (isServiceRunning) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
                    onClick = {
                        FloatingViewsManager.stopFloatService(context)
                    }
                ) {
                    Text(text = "Remove all", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}