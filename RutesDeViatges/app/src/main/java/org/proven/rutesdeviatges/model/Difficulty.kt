package org.proven.rutesdeviatges.model

/**
 * Nivell de dificultat d'una ruta (Fàcil, Moderada, Exigent).
 * S'utilitza al formulari (FilterChips) i a la postal (insignia).
 */
enum class Difficulty(val label: String) {
    FACIL("Fàcil"),
    MODERADA("Moderada"),
    EXIGENT("Exigent")
}
