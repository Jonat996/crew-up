package com.crewup.myapplication.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.PlanViewModel
import com.crewup.myapplication.viewmodel.UploadState

@Composable
fun ImageUploadComponent(
    viewModel: PlanViewModel,
    planId: String
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val uploadState by viewModel.uploadState.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    indication = null, // Desactiva el ripple para un diseño más limpio
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect { interaction ->
                                    if (interaction is PressInteraction.Press) {
                                        // Feedback visual al hacer clic (puedes ajustar)
                                    }
                                }
                            }
                        }
                ),
            elevation = CardDefaults.cardElevation(4.dp),
            border = BorderStroke(1.dp, Color.Gray) // Borde para visibilidad
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = stringResource(R.string.plan_image_desc),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = stringResource(R.string.selected_image_desc),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = stringResource(R.string.add_plan_photo),
                        style = MaterialTheme.typography.headlineSmall, // Texto más grande para visibilidad
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedImageUri != null && uploadState !is UploadState.Loading) {
            Button(
                onClick = { viewModel.uploadImage(planId, selectedImageUri!!) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.upload_button))
            }
        }

        when (uploadState) {
            is UploadState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Subiendo imagen...", style = MaterialTheme.typography.bodyMedium)
            }
            is UploadState.Success -> Text("¡Imagen subida con éxito!", color = MaterialTheme.colorScheme.primary)
            is UploadState.Error -> Text("Error: ${(uploadState as UploadState.Error).message}", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}