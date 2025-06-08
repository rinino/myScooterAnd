// myScooter/app/src/main/java/it/adr/myscooter/ui/scooterlist/AddNewScooterFormScreen.kt
package it.adr.myscooter.ui.scooterlist

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import it.adr.myscooter.data.model.Scooter
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Composable per il form di aggiunta di un nuovo scooter.
 * Corrisponde a AddNewScooterFormView di SwiftUI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScooterFormScreen(
    onSave: (Scooter) -> Unit,
    onCancel: () -> Unit,
    // Questi stati vengono passati da ScooterListScreen (o da un ViewModel più grande)
    newMarca: String,
    onMarcaChange: (String) -> Unit,
    nuovoModello: String,
    onModelloChange: (String) -> Unit,
    nuovaCilindrataString: String,
    onCilindrataStringChange: (String) -> Unit,
    nuovaTarga: String,
    onTargaChange: (String) -> Unit,
    nuovoAnnoString: String,
    onAnnoStringChange: (String) -> Unit,
    nuovoMiscelatore: Boolean,
    onMiscelatoreChange: (Boolean) -> Unit,
    isNuovaCilindrataValid: Boolean, // Questo andrebbe calcolato internamente o da un ViewModel del form
    isNuovaTargaValid: Boolean,     // Come sopra
    isNuovoAnnoValid: Boolean      // Come sopra
) {
    val context = LocalContext.current

    // Stato locale per l'immagine
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) } // Il file salvato

    // Launcher per la selezione dell'immagine
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Effetto per salvare l'immagine quando l'URI cambia
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "scooter_${UUID.randomUUID()}.jpg" // Nome file unico
                val directory = context.filesDir // O getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val file = File(directory, fileName)
                FileOutputStream(file).use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
                inputStream?.close()
                selectedImageFile = file
            } catch (e: Exception) {
                // Gestisci errore, es. Log.e o Toast
                selectedImageFile = null
            }
        }
    }


    // Validazione dei campi numerici e della targa (semplificata, la logica vera sarebbe nel ViewModel)
    val parsedCilindrata = nuovaCilindrataString.toIntOrNull()
    val parsedAnno = nuovoAnnoString.toIntOrNull()

    val isFormValid by remember {
        derivedStateOf {
            newMarca.isNotBlank() &&
                    nuovoModello.isNotBlank() &&
                    parsedCilindrata != null && isNuovaCilindrataValid &&
                    nuovaTarga.isNotBlank() && isNuovaTargaValid &&
                    parsedAnno != null && isNuovoAnnoValid
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Aggiungi Nuovo Scooter") },
            navigationIcon = {
                IconButton(onClick = onCancel) {
                    Icon(Icons.Filled.Close, contentDescription = "Annulla")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campi di input
        OutlinedTextField(
            value = newMarca,
            onValueChange = onMarcaChange,
            label = { Text("Marca") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nuovoModello,
            onValueChange = onModelloChange,
            label = { Text("Modello") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nuovaCilindrataString,
            onValueChange = { newValue ->
                // Permetti solo numeri
                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                    onCilindrataStringChange(newValue)
                    // isNuovaCilindrataValid = (newValue.toIntOrNull() != null) // Valida qui se vuoi
                }
            },
            label = { Text("Cilindrata") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !isNuovaCilindrataValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nuovaTarga,
            onValueChange = onTargaChange,
            label = { Text("Targa") },
            isError = !isNuovaTargaValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nuovoAnnoString,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                    onAnnoStringChange(newValue)
                    // isNuovoAnnoValid = (newValue.toIntOrNull() != null) // Valida qui se vuoi
                }
            },
            label = { Text("Anno") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !isNuovoAnnoValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Miscelatore", modifier = Modifier.weight(1f))
            Switch(checked = nuovoMiscelatore, onCheckedChange = onMiscelatoreChange)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Selezione Immagine
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Immagine Scooter Selezionata",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Clicca per cambiare immagine")
            } else {
                Button(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                    Text("Seleziona Immagine Scooter")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Pulsante Salva
        Button(
            onClick = {
                val newScooter = Scooter(
                    marca = newMarca,
                    modello = nuovoModello,
                    cilindrata = parsedCilindrata ?: 0, // Assicurati di gestire il caso null
                    targa = nuovaTarga,
                    anno = parsedAnno ?: 0, // Assicurati di gestire il caso null
                    miscelatore = nuovoMiscelatore,
                    imgName = selectedImageFile?.name // Salva solo il nome del file
                )
                onSave(newScooter)
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Salva Scooter")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddNewScooterFormScreen() {
    MaterialTheme {
        AddNewScooterFormScreen(
            onSave = {},
            onCancel = {},
            newMarca = "Vespa",
            onMarcaChange = {},
            nuovoModello = "PX",
            onModelloChange = {},
            nuovaCilindrataString = "125",
            onCilindrataStringChange = {},
            nuovaTarga = "AB123CD",
            onTargaChange = {},
            nuovoAnnoString = "1980",
            onAnnoStringChange = {},
            nuovoMiscelatore = true,
            onMiscelatoreChange = {},
            isNuovaCilindrataValid = true,
            isNuovaTargaValid = true,
            isNuovoAnnoValid = true
        )
    }
}