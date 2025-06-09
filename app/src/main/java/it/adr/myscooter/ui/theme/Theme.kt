package it.adr.myscooter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat // Importa WindowCompat

// Definiamo una palette di colori scuri personalizzata
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),      // Viola chiaro per gli elementi primari (es. FAB, bottoni)
    secondary = Color(0xFF03DAC5),    // Turchese per gli elementi secondari
    tertiary = Color(0xFF3700B3),     // Viola scuro per gli elementi terziari (se usati)

    // Colori chiave per lo sfondo nero
    background = Color.Black,         // Sfondo principale dell'app
    surface = Color.Black,            // Sfondi di componenti (Card, Dialoghi, BottomSheet)
    error = Color(0xFFB00020),        // Rosso scuro per i messaggi di errore

    // Colori "on" per il testo e le icone che stanno "sopra" i colori definiti
    onPrimary = Color.Black,          // Testo/icone su primary
    onSecondary = Color.Black,        // Testo/icone su secondary
    onBackground = Color.White,       // Testo/icone su sfondo nero
    onSurface = Color.White,          // Testo/icone su superfici nere
    onError = Color.White             // Testo/icone su errore
)

// Poiché vogliamo uno sfondo nero fisso, potremmo non usare questa palette chiara.
// Tuttavia, è buona pratica mantenerla per completezza o se in futuro volessi un tema chiaro.
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFB00020)
)

@Composable
fun MyScooterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Puoi forzare `true` qui per avere sempre il tema scuro
    dynamicColor: Boolean = false, // Imposta a `false` per non usare i colori dinamici di Android 12+
    content: @Composable () -> Unit
) {
    // Sceglie la palette di colori.
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Effetti collaterali per configurare la status bar e navigation bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Questa è la riga chiave: estende il contenuto dell'app sotto le system bars.
            // Il colore delle system bars sarà quindi gestito dal tuo tema e dai tuoi Composable (es. TopAppBar)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Controlla il colore delle icone nella status bar (scure su sfondo chiaro, chiare su sfondo scuro)
            // Se darkTheme è true (sfondo scuro), vogliamo icone chiare. isAppearanceLightStatusBars = false (icone scure)
            // Se darkTheme è false (sfondo chiaro), vogliamo icone scure. isAppearanceLightStatusBars = true (icone chiare)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Applica il tema Material Design ai Composable figli
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assicurati che `Typography` sia definita in `Type.kt`
        content = content
    )
}