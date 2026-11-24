package org.proven.rutesdeviatges.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.proven.rutesdeviatges.viewmodel.TravelDiaryViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PostalScreen(
    viewModel: TravelDiaryViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val entry = state.selectedRoute
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri: Uri? ->
        if (uri != null && entry != null) {
            val text = viewModel.buildChronicleText(entry)
            try {
                context.contentResolver.openOutputStream(uri)?.use { os ->
                    os.write(text.toByteArray(Charsets.UTF_8))
                }
            } catch (_: Exception) {
                // Ignorar errors d'escriptura en aquesta versió didàctica.
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Postal / Crònica") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (entry == null) {
                Text("No hi ha cap crònica generada.")
                OutlinedButton(onClick = onBack) { Text("Tornar") }
                return@Column
            }

            // Targeta visual de viatge
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = entry.routeName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.destination,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Data: ${entry.date}")
                    Text("Dificultat: ${entry.difficulty.label}")
                    Text("Pressupost: ${"%.2f".format(entry.estimatedBudget)} €")
                    Text("En solitari: ${if (entry.isSolo) "Sí" else "No"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.notes.ifBlank { "(Sense notes)" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Exportar (Storage Access Framework)
            Button(
                onClick = {
                    exportLauncher.launch("cronica_${entry.routeName.replace(" ", "_")}.md")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exportar Crònica (.md / .txt)")
            }

            // Compartir
            OutlinedButton(
                onClick = {
                    val text = viewModel.buildChronicleText(entry)
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                        putExtra(Intent.EXTRA_SUBJECT, "Crònica: ${entry.routeName}")
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Compartir crònica"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartir")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tornar al formulari")
            }
        }
    }
}
