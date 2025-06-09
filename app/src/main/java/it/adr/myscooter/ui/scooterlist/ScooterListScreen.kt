package it.adr.myscooter.ui.scooterlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border // Importa per il bordo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape // Importa per il bordo arrotondato
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Importa per il clipping della forma
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.adr.myscooter.R
import it.adr.myscooter.ui.theme.MyScooterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScooterListScreen(navController: NavController) {
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
            // Immagine nella parte superiore con dimensioni fisse, contorno e bordi arrotondati
            Image(
                painter = painterResource(id = R.drawable.loghetto1_scritta128),
                contentDescription = "Logo My Scooter",
                modifier = Modifier
                    .size(110.dp) // Dimensione 110x110dp
                    .padding(top = 16.dp, bottom = 8.dp) // Padding esterno
                    .border(
                        width = 2.dp, // Larghezza del bordo
                        color = Color.Black, // Colore del bordo nero
                        shape = RoundedCornerShape(15.dp) // Forma del bordo arrotondata a 10dp
                    )
                    .clip(RoundedCornerShape(15.dp)), // Taglia l'immagine stessa con la stessa forma arrotondata
                contentScale = ContentScale.Fit
            )

            // Il resto del contenuto della schermata
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Benvenuto nella ScooterListScreen!\n(Qui ci sarà la lista degli scooter)",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
@Composable
fun ScooterListScreenPreview() {
    MyScooterTheme(darkTheme = true) {
        ScooterListScreen(navController = rememberNavController())
    }
}