package org.proven.rutesdeviatges.model

import com.google.gson.annotations.SerializedName

/**
 * Model immutable que representa una ruta/escapada guardada a l'historic.
 * S'utilitza Gson per persistir en JSON (emmagatzematge intern, filesDir).
 * @JvmOverloads permet a Gson instanciar la classe amb constructor sense arguments.
 */
data class RouteEntry @JvmOverloads constructor(
    val id: String = java.util.UUID.randomUUID().toString(),
    val routeName: String = "",
    val destination: String = "",
    val difficulty: Difficulty = Difficulty.FACIL,
    val estimatedBudget: Double = 0.0,
    val date: String = "",
    val isSolo: Boolean = false,
    val notes: String = ""
)
