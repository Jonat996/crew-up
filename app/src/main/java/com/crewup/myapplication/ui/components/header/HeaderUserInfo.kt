package com.crewup.myapplication.ui.components.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.R
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.UserViewModel

@Composable
fun HeaderUserInfo(
    navController: NavController? = null,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    val firebaseUser = authState.user
    val firestoreUser = userState.user

    val noName = stringResource(R.string.no_name)
    val noOccupation = stringResource(R.string.no_occupation)
    val noLocation = stringResource(R.string.no_location)

    HeaderBase(navController = navController) {
        if (firebaseUser != null) {
            HeaderUserPhoto(
                title = firebaseUser.displayName ?: noName,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar ocupación desde Firestore
            if (userState.isLoading && firestoreUser == null) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.height(16.dp)
                )
            } else {
                val occupation = firestoreUser?.occupation?.takeIf { it.isNotEmpty() } ?: noOccupation
                val location = if (!firestoreUser?.city.isNullOrEmpty() && !firestoreUser?.country.isNullOrEmpty()) {
                    "${firestoreUser?.city}, ${firestoreUser?.country}"
                } else if (!firestoreUser?.city.isNullOrEmpty()) {
                    firestoreUser?.city ?: ""
                } else if (!firestoreUser?.country.isNullOrEmpty()) {
                    firestoreUser?.country ?: ""
                } else {
                    noLocation
                }

                Text(
                    text = occupation,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Localización con icono
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Ubicación",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = location,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HeaderUserInfoPreview() {
    HeaderUserInfo()
}
