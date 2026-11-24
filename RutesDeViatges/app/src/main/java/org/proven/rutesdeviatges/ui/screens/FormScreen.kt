package org.proven.rutesdeviatges.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.proven.rutesdeviatges.model.Difficulty
import org.proven.rutesdeviatges.viewmodel.TravelDiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: TravelDiaryViewModel,
    onNavigateToPostal: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    // Dialog d'històric de rutes guardades (emmagatzematge intern).
    if (state.showHistory) {
        HistoryDialog(
            history = state.history,
            onDismiss = { viewModel.onShowHistoryChange(false) },
            onChronicleText = { viewModel.buildChronicleText(it) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nova Ruta / Escapada") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nom de la ruta
            OutlinedTextField(
                value = state.routeName,
                onValueChange = viewModel::onRouteNameChange,
                label = { Text("Nom de la ruta") },
                isError = state.routeNameError != null,
                supportingText = { state.routeNameError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Destí
            OutlinedTextField(
                value = state.destination,
                onValueChange = viewModel::onDestinationChange,
                label = { Text("Destí") },
                isError = state.destinationError != null,
                supportingText = { state.destinationError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Nivell de dificultat (FilterChips)
            Text("Nivell de dificultat", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Difficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = state.difficulty == diff,
                        onClick = { viewModel.onDifficultyChange(diff) },
                        label = { Text(diff.label) }
                    )
                }
            }

            // Pressupost estimat
            OutlinedTextField(
                value = state.estimatedBudget,
                onValueChange = viewModel::onBudgetChange,
                label = { Text("Pressupost estimat (€)") },
                isError = state.budgetError != null,
                supportingText = { state.budgetError?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Data de la ruta (DatePicker)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.date,
                    onValueChange = {},
                    label = { Text("Data de la ruta") },
                    isError = state.dateError != null,
                    supportingText = { state.dateError?.let { Text(it) } },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    singleLine = true
                )
                Button(onClick = { showDatePicker = true }) {
                    Text("Seleccionar")
                }
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                viewModel.onDateChange(viewModel.formatMillis(millis))
                            }
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel·lar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Viatge en solitari (Switch)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = state.isSolo,
                    onCheckedChange = viewModel::onIsSoloChange
                )
                Text("Ruta feta en solitari")
            }

            // Notes multilínia
            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes de l'experiència") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Acció principal
            Button(
                onClick = {
                    val entry = viewModel.generateChronicle()
                    if (entry != null) onNavigateToPostal()
                },
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar Postal / Crònica")
            }

            // Accés a l'històric
            TextButton(
                onClick = { viewModel.onShowHistoryChange(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar històric de rutes")
            }
        }
    }
}

@Composable
private fun HistoryDialog(
    history: List<org.proven.rutesdeviatges.model.RouteEntry>,
    onDismiss: () -> Unit,
    onChronicleText: (org.proven.rutesdeviatges.model.RouteEntry) -> String
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Històric de rutes") },
        text = {
            if (history.isEmpty()) {
                Text("Encara no hi ha rutes guardades.")
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    history.forEach { entry ->
                        Text(
                            text = "${entry.date} - ${entry.routeName} (${entry.destination})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tancar") }
        }
    )
}
