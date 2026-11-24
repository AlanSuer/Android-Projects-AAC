package org.proven.rutesdeviatges.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.proven.rutesdeviatges.model.Difficulty
import org.proven.rutesdeviatges.model.RouteEntry
import org.proven.rutesdeviatges.model.TravelDiaryUiState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * ViewModel del Diari de Viatges.
 * Gestiona l'estat reactiu (MutableStateFlow privat / StateFlow públic) i la persistència interna.
 */
class TravelDiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TravelDiaryUiState())
    val uiState: StateFlow<TravelDiaryUiState> = _uiState.asStateFlow()

    private val gson = Gson()
    private val storageFile: File = File(application.filesDir, "diari_viatges.json")

    init {
        // En obrir l'app, carreguem l'històric persistit a filesDir.
        loadHistory()
    }

    // ---------- Setters amb validació en temps real ----------

    fun onRouteNameChange(value: String) {
        updateState { it.copy(routeName = value, routeNameError = validateName(value)) }
    }

    fun onDestinationChange(value: String) {
        updateState { it.copy(destination = value, destinationError = validateName(value)) }
    }

    fun onDifficultyChange(value: Difficulty) {
        updateState { it.copy(difficulty = value) }
    }

    fun onBudgetChange(value: String) {
        updateState { it.copy(estimatedBudget = value, budgetError = validateBudget(value)) }
    }

    fun onDateChange(value: String) {
        updateState { it.copy(date = value, dateError = validateDate(value)) }
    }

    fun onIsSoloChange(value: Boolean) {
        updateState { it.copy(isSolo = value) }
    }

    fun onNotesChange(value: String) {
        updateState { it.copy(notes = value) }
    }

    fun onShowHistoryChange(show: Boolean) {
        _uiState.update { it.copy(showHistory = show) }
    }

    // ---------- Validació ----------

    private fun validateName(value: String): String? =
        if (value.isBlank()) "Camp obligatori" else null

    private fun validateBudget(value: String): String? {
        if (value.isBlank()) return "Camp obligatori"
        val parsed = value.toDoubleOrNull()
        if (parsed == null) return "Pressupost invàlid"
        if (parsed <= 0.0) return "Ha de ser major que 0"
        return null
    }

    private fun validateDate(value: String): String? =
        if (value.isBlank()) "Selecciona una data" else null

    private fun parseBudget(text: String): Double? {
        val v = text.toDoubleOrNull() ?: return null
        return if (v > 0.0) v else null
    }

    // ---------- Actualització immutable de l'estat ----------

    private fun updateState(transform: (TravelDiaryUiState) -> TravelDiaryUiState) {
        _uiState.update { old ->
            val updated = transform(old)
            val valid = updated.routeName.isNotBlank() &&
                updated.destination.isNotBlank() &&
                updated.date.isNotBlank() &&
                updated.budgetError == null &&
                parseBudget(updated.estimatedBudget) != null
            updated.copy(isFormValid = valid)
        }
    }

    // ---------- Persistència interna (filesDir) ----------

    private fun loadHistory() {
        try {
            if (!storageFile.exists()) return
            val json = storageFile.readText()
            val type = object : TypeToken<List<RouteEntry>>() {}.type
            val list: List<RouteEntry>? = gson.fromJson(json, type)
            _uiState.update { it.copy(history = list ?: emptyList()) }
        } catch (e: Exception) {
            // Si el fitxer està corrupte, comencem buit.
            _uiState.update { it.copy(history = emptyList()) }
        }
    }

    private fun saveHistory(list: List<RouteEntry>) {
        try {
            storageFile.writeText(gson.toJson(list))
        } catch (_: Exception) {
            // Ignorar errors d'escriptura en aquesta versió didàctica.
        }
    }

    // ---------- Accions ----------

    /**
     * Genera la crònica: crea un RouteEntry, l'afegeix a l'històric i el persisteix.
     * Retorna l'entrada creada perquè la vista pugui navegar.
     */
    fun generateChronicle(): RouteEntry? {
        val s = _uiState.value
        if (!s.isFormValid) return null
        val budget = parseBudget(s.estimatedBudget) ?: return null
        val entry = RouteEntry(
            routeName = s.routeName.trim(),
            destination = s.destination.trim(),
            difficulty = s.difficulty,
            estimatedBudget = budget,
            date = s.date,
            isSolo = s.isSolo,
            notes = s.notes.trim()
        )
        val newHistory = s.history + entry
        _uiState.update { it.copy(history = newHistory, selectedRoute = entry) }
        saveHistory(newHistory)
        return entry
    }

    /**
     * Text formatat de la crònica (Markdown) per exportar/compartir.
     */
    fun buildChronicleText(entry: RouteEntry): String {
        val solo = if (entry.isSolo) "Sí (en solitari)" else "No (acompanyat/da)"
        return buildString {
            appendLine("# Crònica de Ruta: ${entry.routeName}")
            appendLine()
            appendLine("**Destí:** ${entry.destination}")
            appendLine("**Data:** ${entry.date}")
            appendLine("**Dificultat:** ${entry.difficulty.label}")
            appendLine("**Pressupost estimat:** ${"%.2f".format(entry.estimatedBudget)} €")
            appendLine("**Viatge en solitari:** $solo")
            appendLine()
            appendLine("## Relat de l'experiència")
            appendLine(entry.notes.ifBlank { "(Sense notes)" })
        }
    }

    /**
     * Formata mil·lisegons (DatePicker) a text dd/MM/yyyy.
     */
    fun formatMillis(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}
