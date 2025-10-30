package com.crewup.myapplication.ui.components.header
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.UserViewModel

@Composable
fun HeaderUserPhoto(
    title: String? = null,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    val firebaseUser = authState.user
    val firestoreUser = userState.user

    // Obtener foto de perfil: primero intenta desde Firestore, luego desde Firebase Auth
    val photoUrl = when {
        !firestoreUser?.photoUrl.isNullOrBlank() -> firestoreUser?.photoUrl
        firebaseUser?.photoUrl != null -> firebaseUser.photoUrl.toString()
        else -> "undefined"
    }

    // Obtener nombre: si se pasa title, usar ese; sino obtener desde Firestore/Firebase Auth
    val displayName = title ?: when {
        !firestoreUser?.name.isNullOrEmpty() && !firestoreUser?.lastName.isNullOrEmpty() ->
            "${firestoreUser?.name} ${firestoreUser?.lastName}"
        !firestoreUser?.name.isNullOrEmpty() ->
            firestoreUser?.name
        firebaseUser?.displayName != null ->
            firebaseUser.displayName
        else ->
            "Sin nombre"
    }

    if (photoUrl == "undefined") {
        Image(
            painter = painterResource(id = R.drawable.ic_pizza_logo),
            contentDescription = "Logo por defecto",
            modifier = Modifier.size(90.dp)
        )
    } else {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(90.dp).clip(CircleShape)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = displayName ?: "Sin nombre",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Preview
@Composable
fun previewHeaderUserPhoto(){
    HeaderUserPhoto(title = "Juan Monroy")
}