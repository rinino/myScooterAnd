package it.adr.myscooter.ui.scooterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Per ottenere il ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.ui.navigation.Screen // Assicurati di avere una classe Screen per le rotte
import kotlinx.coroutines.launch

/**
 * Composable principale per la schermata della lista degli scooter.
 * Visualizza la lista degli scooter e permette l'aggiunta/eliminazione.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScooterListScreen(
    navController: NavController,
    viewModel: ScooterListViewModel = viewModel() // Inietta il ViewModel
) {
    // Osserva gli StateFlow del ViewModel
    val scooters by viewModel.scooters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showAlert by viewModel.showAlert.collectAsState()
    val alertMessage by viewModel.alertMessage.collectAsState()

    // Stato per il Snackbar (messaggi temporanei)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Stato per la visibilità del form di aggiunta (Sheet)
    var showAddScooterSheet by remember { mutableStateOf(false) }

    // Dati per il nuovo scooter nel form (verranno passati al form)
    // Questi @State variables andrebbero spostati in un ViewModel dedicato al form,
    // o integrati in questo ViewModel se il form è semplice.
    // Per ora, li gestiamo qui per replicare la struttura del tuo SwiftUI.
    var newMarca by remember { mutableStateOf("") }
    var nuovoModello by remember { mutableStateOf("") }
    var nuovaCilindrataString by remember { mutableStateOf("") }
    var nuovaTarga by remember { mutableStateOf("") }
    var nuovoAnnoString by remember { mutableStateOf("") }
    var nuovoMiscelatore by remember { mutableStateOf(false) }
    // La gestione delle immagini è più complessa in Compose/Android,
    // Richiede PhotosPicker o simili e URI/Bitmap. Lasciamo i placeholder per ora.
    // var nuovoSelectedItem: PhotosPickerItem? = null
    // var nuovoSelectedImage: Image? = null
    // var nuovoSelectedImageData: Data? = null
    var isNuovaCilindrataValid by remember { mutableStateOf(true) }
    var isNuovaTargaValid by remember { mutableStateOf(true) }
    var isNuovoAnnoValid by remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Scooter") }, // TestiGenerici.myScooter
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = { showAddScooterSheet = true },
                        enabled = !isLoading // Disabilita se in caricamento
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Aggiungi Scooter")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (scooters.isEmpty() && !isLoading) {
                // Messaggio per lista vuota
                Text(
                    text = "Nessuno scooter disponibile. Premi '+' per aggiungerne uno!",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = !isLoading // Disabilita scroll se in caricamento
                ) {
                    items(items = scooters, key = { it.id }) { scooter ->
                        ScooterRow(
                            scooter = scooter,
                            onItemClick = {
                                // Naviga alla schermata di dettaglio
                                navController.navigate(Screen.ScooterDetail.route + "/${scooter.id}")
                            },
                            onDeleteClick = {
                                // Chiama la funzione di delete nel ViewModel
                                viewModel.deleteScooter(scooter)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Scooter ${scooter.targa} eliminato.")
                                }
                            }
                        )
                    }
                }
            }

            // Indicatore di caricamento al centro
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Dialog per gli alert
            if (showAlert) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissAlert() },
                    title = { Text("Informazioni") }, // TestiGenerici.infoM
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

    // Sheet per aggiungere un nuovo scooter (da implementare separatamente)
    // Questa è una semplificazione. In Compose, un Sheet può essere un Composable separato
    // che viene mostrato condizionalmente, o un BottomSheetScaffold.
    // Per una replica fedele dello Sheet di SwiftUI, si può usare ModalBottomSheet.
    if (showAddScooterSheet) {
        // TODO: Implementa AddNewScooterFormScreen come Composable
        // Per ora, un semplice Text per simulare l'apertura del form.
        AddNewScooterFormScreen(
            onSave = { newScooter ->
                viewModel.saveNewScooter(newScooter)
                showAddScooterSheet = false // Chiudi il sheet dopo il salvataggio
                // TODO: resetta i campi del form qui o nel form stesso
                newMarca = ""
                nuovoModello = ""
                nuovaCilindrataString = ""
                nuovaTarga = ""
                nuovoAnnoString = ""
                nuovoMiscelatore = false
            },
            onCancel = {
                showAddScooterSheet = false
                // TODO: resetta i campi del form
                newMarca = ""
                nuovoModello = ""
                nuovaCilindrataString = ""
                nuovaTarga = ""
                nuovoAnnoString = ""
                nuovoMiscelatore = false
            },
            // Passa gli stati come binding o come State<T>/MutableState<T>
            newMarca = newMarca,
            onMarcaChange = { newMarca = it },
            nuovoModello = nuovoModello,
            onModelloChange = { nuovoModello = it },
            nuovaCilindrataString = nuovaCilindrataString,
            onCilindrataStringChange = { nuovaCilindrataString = it },
            nuovaTarga = nuovaTarga,
            onTargaChange = { nuovaTarga = it },
            nuovoAnnoString = nuovoAnnoString,
            onAnnoStringChange = { nuovoAnnoString = it },
            nuovoMiscelatore = nuovoMiscelatore,
            onMiscelatoreChange = { nuovoMiscelatore = it },
            isNuovaCilindrataValid = isNuovaCilindrataValid,
            isNuovaTargaValid = isNuovaTargaValid,
            isNuovoAnnoValid = isNuovoAnnoValid
        )
    }
}

/**
 * Composable per la riga di un singolo scooter nella lista.
 * Replica ScooterRowView di SwiftUI.
 */
@Composable
fun ScooterRow(
    scooter: Scooter,
    onItemClick: (Scooter) -> Unit,
    onDeleteClick: (Scooter) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(scooter) },
        headlineContent = { Text("${scooter.marca} ${scooter.modello}") },
        supportingContent = { Text("Targa: ${scooter.targa} - Anno: ${scooter.anno}") },
        trailingContent = {
            IconButton(onClick = { onDeleteClick(scooter) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Elimina Scooter",
                    tint = Color.Red
                )
            }
        }
        // TODO: Aggiungi qui un'immagine se disponibile (richiede Coil/Glide per caricamento)
        // leadingContent = {
        //     if (scooter.imgName != null) {
        //         // Immagine dello scooter
        //     } else {
        //         // Placeholder
        //     }
        // }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewScooterListScreen() {
    MaterialTheme {
        val dummyScooters = listOf(
            Scooter(id = 1, marca = "Vespa", modello = "PX 125", cilindrata = 125, targa = "AA123BB", anno = 1980, miscelatore = false, imgName = null),
            Scooter(id = 2, marca = "Lambretta", modello = "Li 150", cilindrata = 150, targa = "CC456DD", anno = 1965, miscelatore = true, imgName = null),
            Scooter(id = 3, marca = "Piaggio", modello = "Zip", cilindrata = 50, targa = "EE789FF", anno = 2005, miscelatore = true, imgName = null)
        )
        // Per mostrare una preview senza un vero ViewModel, potresti creare una versione semplificata:
        Column {
            Text("Anteprima Lista Scooter", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
            LazyColumn {
                items(dummyScooters) { scooter ->
                    ScooterRow(
                        scooter = scooter,
                        onItemClick = { /* Do nothing in preview */ },
                        onDeleteClick = { /* Do nothing in preview */ }
                    )
                }
            }
        }
    }
}