// myScooter/app/src/main/java/it/adr/myscooter/ui/scooterdetail/ScooterDetailScreen.kt
package it.adr.myscooter.ui.scooterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.adr.myscooter.data.model.Scooter // Importa la tua data class Scooter

/**
 * Composable per la schermata dei dettagli di un singolo scooter.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScooterDetailScreen(
    navController: NavController,
    scooterId: Long, // L'ID dello scooter passato dalla navigazione
    viewModel: ScooterDetailViewModel = viewModel() // Inietta il ViewModel
) {
    // Osserva gli stati dal ViewModel
    val scooter by viewModel.scooter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showAlert by viewModel.showAlert.collectAsState()
    val alertMessage by viewModel.alertMessage.collectAsState()

    // Stati locali per i campi di modifica (inizializzati con i valori dello scooter)
    // Questi stati vengono aggiornati quando lo scooter viene caricato
    var marca by remember(scooter) { mutableStateOf(scooter?.marca ?: "") }
    var modello by remember(scooter) { mutableStateOf(scooter?.modello ?: "") }
    var cilindrataString by remember(scooter) { mutableStateOf(scooter?.cilindrata?.toString() ?: "") }
    var targa by remember(scooter) { mutableStateOf(scooter?.targa ?: "") }
    var annoString by remember(scooter) { mutableStateOf(scooter?.anno?.toString() ?: "") }
    var miscelatore by remember(scooter) { mutableStateOf(scooter?.miscelatore ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(scooter?.let { "${it.marca} ${it.modello}" } ?: "Dettagli Scooter") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (scooter == null) {
                // Se lo scooter è nullo e non sta caricando, mostra un messaggio di errore
                Text(
                    text = alertMessage.ifEmpty { "Scooter non trovato o ID non valido." },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Campi per la modifica
                    OutlinedTextField(
                        value = marca,
                        onValueChange = { marca = it },
                        label = { Text("Marca") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = modello,
                        onValueChange = { modello = it },
                        label = { Text("Modello") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cilindrataString,
                        onValueChange = { cilindrataString = it },
                        label = { Text("Cilindrata") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = targa,
                        onValueChange = { targa = it },
                        label = { Text("Targa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = annoString,
                        onValueChange = { annoString = it },
                        label = { Text("Anno") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Miscelatore:")
                        Checkbox(
                            checked = miscelatore,
                            onCheckedChange = { miscelatore = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pulsante Salva
                    Button(
                        onClick = {
                            // Cerca di convertire i valori stringa in Int
                            val cilindrata = cilindrataString.toIntOrNull()
                            val anno = annoString.toIntOrNull()

                            if (cilindrata != null && anno != null) {
                                val updatedScooter = scooter!!.copy(
                                    marca = marca,
                                    modello = modello,
                                    cilindrata = cilindrata,
                                    targa = targa,
                                    anno = anno,
                                    miscelatore = miscelatore
                                )
                                viewModel.updateScooter(updatedScooter)
                                navController.popBackStack() // Torna indietro dopo il salvataggio
                            } else {
                                // TODO: Mostra un messaggio di errore all'utente per input non validi
                                viewModel.showAlert("Assicurati che Cilindrata e Anno siano numeri validi.")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && scooter != null // Abilita solo se non sta caricando e scooter è valido
                    ) {
                        Text("Salva Modifiche")
                    }

                    // TODO: Aggiungi qui la logica per la selezione/visualizzazione delle immagini
                }
            }

            // Dialog per gli alert
            if (showAlert) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissAlert() },
                    title = { Text("Informazioni") },
                    text = { Text(alertMessage) },
                    confirmButton = {
                        Button(onClick = { viewModel.dismissAlert() }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScooterDetailScreen() {
    MaterialTheme {
        // Per la preview, dobbiamo simulare un NavController e un ID valido
        val dummyScooter = Scooter(
            id = 1,
            marca = "Vespa",
            modello = "PX 125",
            cilindrata = 125,
            targa = "AA123BB",
            anno = 1980,
            miscelatore = false,
            imgName = null
        )

        // Per una preview realistica, sarebbe meglio mockare il ViewModel o i dati
        // Qui simuleremo solo la UI.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Anteprima Dettagli Scooter", style = MaterialTheme.typography.headlineMedium)
            Text("Marca: ${dummyScooter.marca}")
            Text("Modello: ${dummyScooter.modello}")
            Text("Cilindrata: ${dummyScooter.cilindrata}")
            Text("Targa: ${dummyScooter.targa}")
            Text("Anno: ${dummyScooter.anno}")
            Text("Miscelatore: ${if (dummyScooter.miscelatore) "Sì" else "No"}")
            // Aggiungi qui altri elementi UI che vuoi visualizzare nella preview
        }
    }
}