package it.adr.myscooter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.adr.myscooter.ui.navigation.Screen
import it.adr.myscooter.ui.scooterlist.ScooterListScreen
import it.adr.myscooter.ui.theme.MyScooterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyScooterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScooterApp()
                }
            }
        }
    }
}

@Composable
fun MyScooterApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ScooterList.route // Aggiorna la rotta di partenza
    ) {
        // Definisci la rotta per la schermata della lista scooter
        composable(route = Screen.ScooterList.route) {
            ScooterListScreen(navController = navController) // Usa il nuovo Composable
        }
        // Altre rotte verranno aggiunte qui in futuro (es. dettaglio scooter, aggiungi scooter)
        // composable(route = Screen.ScooterDetail.route + "/{scooterId}") { backStackEntry ->
        //     val scooterId = backStackEntry.arguments?.getString("scooterId")?.toIntOrNull()
        //     // ... passa scooterId al Composable del dettaglio
        // }
    }
}
