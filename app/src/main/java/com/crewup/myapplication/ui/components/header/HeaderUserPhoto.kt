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
import coil.compose.AsyncImage
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.AuthViewModel

@Composable
fun HeaderUserPhoto(title: String, authViewModel: AuthViewModel = viewModel()) {

    val authState by authViewModel.authState.collectAsState()
    val user = authState.user
    val photoUrl =user?.photoUrl ?: "undefined"

    HeaderBase {
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
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun previewHeaderUserPhoto(){
    HeaderUserPhoto(title = "Juan Monroy")
}