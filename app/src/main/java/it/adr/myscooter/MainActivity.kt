// myScooter/app/src/main/java/it/adr/myscooter/MainActivity.kt
package it.adr.myscooter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.adr.myscooter.ui.navigation.Screen
import it.adr.myscooter.ui.scooterlist.ScooterListScreen
import it.adr.myscooter.ui.scooterdetail.ScooterDetailScreen // Dovrai creare questa
import it.adr.myscooter.ui.theme.MyScooterTheme // Il tuo tema

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
    NavHost(navController = navController, startDestination = Screen.ScooterList.route) {
        composable(Screen.ScooterList.route) {
            ScooterListScreen(navController = navController)
        }
        composable(
            route = Screen.ScooterDetail.route,
            arguments = listOf(navArgument("scooterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val scooterId = backStackEntry.arguments?.getLong("scooterId")
            if (scooterId != null) {
                ScooterDetailScreen(navController = navController, scooterId = scooterId)
            } else {
                // Gestisci il caso in cui l'ID non è presente (es. mostra un errore o torna indietro)
            }
        }
        // TODO: Aggiungi qui la rotta per AddNewScooterFormScreen se lo rendi una rotta separata
        // Altrimenti, viene visualizzato come un Composable modale all'interno di ScooterListScreen
    }
}