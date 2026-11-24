package org.proven.rutesdeviatges.ui.navigation

/**
 * Rutes de navegació de l'app (Navigation Compose).
 */
sealed class Screen(val route: String) {
    data object Form : Screen("form")
    data object Postal : Screen("postal")
}
