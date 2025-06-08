// myScooter/app/src/main/java/it/adr/myscooter/ui/navigation/Screen.kt
package it.adr.myscooter.ui.navigation

/**
 * Sealed class che definisce tutte le rotte (schermate) della tua applicazione.
 * Questo aiuta a gestire la navigazione in modo sicuro e organizzato.
 */
sealed class Screen(val route: String) {
    // Oggetto per la schermata della lista degli scooter
    object ScooterList : Screen("scooter_list")

    // Oggetto per la schermata dei dettagli dello scooter.
    // Accetta un argomento 'scooterId' che è l'ID dello scooter da visualizzare.
    object ScooterDetail : Screen("scooter_detail/{scooterId}") {
        /**
         * Crea la rotta completa per la schermata dei dettagli,
         * includendo l'ID dello scooter come argomento.
         * @param scooterId L'ID dello scooter da passare.
         * @return La stringa della rotta con l'ID inserito.
         */
        fun createRoute(scooterId: Long) = "scooter_detail/$scooterId"
    }

    // Aggiungi qui altre rotte se necessario, ad esempio:
    // object Settings : Screen("settings_screen")
    // object About : Screen("about_screen")
}