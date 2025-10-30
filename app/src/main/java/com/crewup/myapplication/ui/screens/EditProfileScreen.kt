package com.crewup.myapplication.ui.screens


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.crewup.myapplication.R
import com.crewup.myapplication.ui.components.header.HeaderEditableUserPhoto
import com.crewup.myapplication.ui.components.sections.EditProfileSection
import com.crewup.myapplication.ui.layout.MainLayout
import com.crewup.myapplication.viewmodel.AuthViewModel
import com.crewup.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val userState by userViewModel.userState.collectAsState()
    val firebaseUser = authState.user
    val firestoreUser = userState.user
    val noName = stringResource(R.string.no_name)

    // Obtener nombre: primero de Firestore, luego de Firebase Auth
    val userName = when {
        !firestoreUser?.name.isNullOrEmpty() && !firestoreUser?.lastName.isNullOrEmpty() ->
            "${firestoreUser?.name} ${firestoreUser?.lastName}"
        !firestoreUser?.name.isNullOrEmpty() ->
            firestoreUser?.name
        firebaseUser?.displayName != null ->
            firebaseUser.displayName
        else ->
            noName
    }

    MainLayout(
        header = {
            HeaderEditableUserPhoto(
                title = userName ?: noName,
                navController = navController,
                userViewModel = userViewModel
            )
        },
        content = {
            EditProfileSection()
        }
    )
}
