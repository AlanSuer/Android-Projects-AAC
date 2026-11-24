package org.proven.rutesdeviatges.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.proven.rutesdeviatges.ui.screens.FormScreen
import org.proven.rutesdeviatges.ui.screens.PostalScreen
import org.proven.rutesdeviatges.viewmodel.TravelDiaryViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: TravelDiaryViewModel) {
    NavHost(navController = navController, startDestination = Screen.Form.route) {
        composable(Screen.Form.route) {
            FormScreen(viewModel = viewModel, onNavigateToPostal = {
                navController.navigate(Screen.Postal.route)
            })
        }
        composable(Screen.Postal.route) {
            PostalScreen(viewModel = viewModel, onBack = {
                navController.popBackStack()
            })
        }
    }
}
