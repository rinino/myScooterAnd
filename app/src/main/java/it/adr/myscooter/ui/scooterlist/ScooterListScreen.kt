package it.adr.myscooter.ui.scooterlist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.adr.myscooter.MyScooterApplication
import it.adr.myscooter.R
import it.adr.myscooter.data.model.Scooter
import it.adr.myscooter.ui.navigation.Screen
import it.adr.myscooter.ui.theme.MyScooterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScooterListScreen(
    navController: NavController,
) {
    // Ottiene il contesto locale dell'applicazione.
    val context = LocalContext.current

    // Accede all'istanza della tua classe Application personalizzata, che contiene il repository.
    val application = context.applicationContext as MyScooterApplication

    // Recupera l'istanza del ScooterRepository direttamente dalla tua MyScooterApplication.
    val scooterRepository = application.scooterRepository

    // Crea un'istanza della tua ViewModel Factory, passandole il repository.
    val factory = ScooterViewModelFactory(scooterRepository)

    // Ottiene il ViewModel usando l'helper `viewModel()` e la tua factory.
    // Questo è il passaggio chiave che permette al sistema di creare/recuperare il ViewModel
    // e di iniettargli il repository correttamente.
    val viewModel: ScooterListViewModel = viewModel(factory = factory)

    // Osserva gli stati del ViewModel come stati Composables.
    val scooters by viewModel.scooters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showAlert by viewModel.showAlert.collectAsState()
    val alertMessage by viewModel.alertMessage.collectAsState()

    // Gestisce gli effetti collaterali, come mostrare un Toast, quando lo stato `showAlert` cambia.
    if (showAlert) {
        LaunchedEffect(alertMessage) {
            Toast.makeText(context, alertMessage, Toast.LENGTH_LONG).show()
            viewModel.dismissAlert() // Resetta lo stato dell'alert dopo averlo mostrato.
        }
    }

    // Struttura principale della UI utilizzando Scaffold per una disposizione Material Design.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Scooter") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("Add Scooter button cliccato!")
                    // Qui puoi aggiungere la logica di navigazione per aggiungere un nuovo scooter.
                    // Esempio: navController.navigate(Screen.AddEditScooter.route)
                },
                modifier = Modifier
                    .offset(x = (-10).dp, y = (-10).dp) // Applica un piccolo offset per il posizionamento visivo.
            ) {
                Icon(Icons.Filled.Add, "Aggiungi Scooter") // Icona per il Floating Action Button.
            }
        }
    ) { paddingValues -> // `paddingValues` è fornito da Scaffold per gestire i padding di topBar e FAB.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Applica i padding forniti da Scaffold.
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sezione del logo e del titolo dell'applicazione.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loghetto1_scritta128), // Assicurati che questa risorsa sia presente nel tuo progetto.
                    contentDescription = "Logo My Scooter",
                    modifier = Modifier
                        .size(115.dp)
                        .align(Alignment.Center) // Centra l'immagine all'interno del Box.
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(11.dp)
                        )
                        .clip(RoundedCornerShape(11.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = "I Miei Scooter",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start) // Allinea il titolo a sinistra.
            )

            // Sezione del contenuto principale: mostra l'indicatore di caricamento, un messaggio
            // se la lista è vuota, o la lista degli scooter.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Occupa lo spazio verticale rimanente.
                contentAlignment = Alignment.Center // Centra il contenuto (indicatore/messaggio).
            ) {
                if (isLoading) {
                    CircularProgressIndicator() // Mostra un indicatore di caricamento.
                } else if (scooters.isEmpty()) {
                    Text(
                        text = "Nessuno scooter disponibile. Premi '+' per aggiungerne uno!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = scooters, key = { it.id }) { scooter ->
                            // Composable per ogni riga di scooter nella lista.
                            ScooterRow(
                                scooter = scooter,
                                onItemClick = {
                                    println("Clicked on scooter: ${scooter.modello}")
                                    // Gestisce il click su un elemento della lista (es. navigazione al dettaglio).
                                    // navController.navigate(Screen.ScooterDetail.route + "/${scooter.id}")
                                },
                                onDeleteClick = {
                                    viewModel.deleteScooter(scooter) // Chiama la funzione di eliminazione nel ViewModel.
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


// --- Note sulla Preview (generalmente commentate per ViewModel con dipendenze) ---
// @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
// @Composable
// fun ScooterListScreenPreview() {
//     MyScooterTheme(darkTheme = true) {
//         // Per le Preview di Composable che usano ViewModel con dipendenze,
//         // è necessario un setup di test più avanzato (es. Mockito, Hilt per i test)
//         // o un ViewModel finto/mockato per la Preview.
//         // Attualmente, questa Preview non funzionerà senza ulteriori configurazioni.
//         // È consigliabile commentare o rimuovere le Preview per Composable complessi.
//         // ScooterListScreen(navController = rememberNavController())
//     }
// }