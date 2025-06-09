package it.adr.myscooter.ui.scooterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import it.adr.myscooter.data.model.Scooter // Importa la tua data class Scooter
import it.adr.myscooter.ui.theme.MyScooterTheme // Importa il tuo tema
import java.io.File

/**
 * Composable per la riga di un singolo scooter nella lista.
 */
@Composable
fun ScooterRow(
    scooter: Scooter,
    onItemClick: (Scooter) -> Unit,
    onDeleteClick: (Scooter) -> Unit
) {
    val context = LocalContext.current // Ottieni il contesto locale

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
        },
        leadingContent = {
            // Carica l'immagine solo se imgName non è nullo
            if (scooter.imgName != null) {
                // Costruisci il percorso completo del file
                val imageFile = File(context.filesDir, scooter.imgName)
                if (imageFile.exists()) {
                    // Utilizza AsyncImage di Coil per caricare l'immagine dal File
                    AsyncImage(
                        model = imageFile, // Passa il File direttamente a Coil
                        contentDescription = "Immagine scooter",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape), // Applica forma circolare
                        contentScale = ContentScale.Crop // Adatta l'immagine al cerchio
                    )
                } else {
                    // Placeholder se il file non esiste
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.LightGray, CircleShape) // Colore di sfondo e forma per il placeholder
                            .clip(CircleShape), // Angoli arrotondati
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Photo, // Icona di placeholder
                            contentDescription = "Nessuna immagine",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            } else {
                // Placeholder se non c'è imgName
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.LightGray, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Photo,
                        contentDescription = "Nessuna immagine",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ScooterRowPreview() {
    MyScooterTheme {
        ScooterRow(
            scooter = Scooter(
                id = 0,
                marca = "Vespa",
                modello = "PX 125",
                cilindrata = 125,
                targa = "AB123CD",
                anno = 1980,
                miscelatore = false,
                imgName = null // O un nome di file fittizio se hai un'immagine di test nei drawables
            ),
            onItemClick = {},
            onDeleteClick = {}
        )
    }
}
