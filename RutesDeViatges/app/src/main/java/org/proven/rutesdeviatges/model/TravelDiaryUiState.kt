package org.proven.rutesdeviatges.model

/**
 * Estat de la UI del Diari de Viatges.
 * Conté els camps del formulari actual, els errors de validació i l'històric de rutes guardades.
 */
data class TravelDiaryUiState(
    // Camps del formulari
    val routeName: String = "",
    val destination: String = "",
    val difficulty: Difficulty = Difficulty.FACIL,
    val estimatedBudget: String = "",   // text editable; es valida i converteix a Double
    val date: String = "",
    val isSolo: Boolean = false,
    val notes: String = "",
    // Errors de validació (null si no hi ha error)
    val routeNameError: String? = null,
    val destinationError: String? = null,
    val budgetError: String? = null,
    val dateError: String? = null,
    // Històric carregat de l'emmagatzematge intern
    val history: List<RouteEntry> = emptyList(),
    // Ruta seleccionada per a la postal (passada a PostalScreen)
    val selectedRoute: RouteEntry? = null,
    // Indica si el formulari és vàlid
    val isFormValid: Boolean = false,
    // Mostrar dialog d'històric
    val showHistory: Boolean = false
)
