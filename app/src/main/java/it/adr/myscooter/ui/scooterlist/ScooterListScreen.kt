package it.adr.myscooter.ui.scooterlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset // <--- NUOVO IMPORT NECESSARIO
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.adr.myscooter.R
import it.adr.myscooter.ui.navigation.Screen
import it.adr.myscooter.ui.theme.MyScooterTheme
import androidx.compose.foundation.layout.offset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScooterListScreen(
    navController: NavController,
    viewModel: ScooterListViewModel = viewModel()
) {
    val scooters by viewModel.scooters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showAlert by viewModel.showAlert.collectAsState()
    val alertMessage by viewModel.alertMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Scooter") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loghetto1_scritta128),
                    contentDescription = "Logo My Scooter",
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.Center) // Rimetto l'immagine al centro del Box
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit
                )

                FloatingActionButton(
                    onClick = {
                        println("Add Scooter button clicked!")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-20).dp, y = 20.dp)
                        .size(38.dp)
                ) {
                    Icon(Icons.Filled.Add, "Aggiungi Scooter")
                }
            }

            Text(
                text = "I Miei Scooter",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
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
                            ScooterRow(
                                scooter = scooter,
                                onItemClick = {
                                    println("Clicked on scooter: ${scooter.targa}")
                                },
                                onDeleteClick = {
                                    viewModel.deleteScooter(scooter)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
@Composable
fun ScooterListScreenPreview() {
    MyScooterTheme(darkTheme = true) {
        ScooterListScreen(navController = rememberNavController(), viewModel = ScooterListViewModel())
    }
}