package org.proven.rutesdeviatges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.proven.rutesdeviatges.ui.navigation.NavGraph
import org.proven.rutesdeviatges.ui.theme.RutesDeViatgesTheme
import org.proven.rutesdeviatges.viewmodel.TravelDiaryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RutesDeViatgesTheme {
                val navController = rememberNavController()
                val viewModel: TravelDiaryViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavGraph(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
