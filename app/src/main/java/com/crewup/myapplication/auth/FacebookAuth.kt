package com.crewup.myapplication.auth

import android.app.Activity
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FacebookAuth(private val activity: Activity) {

    private val auth = FirebaseAuth.getInstance()
    val callbackManager = CallbackManager.Factory.create()

    fun login(onResult: (Boolean, String?) -> Unit) {
        LoginManager.getInstance().logOut()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : com.facebook.FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("FacebookAuth", "Facebook login exitoso, obteniendo credenciales de Firebase")
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FacebookAuth", "Login Firebase exitoso: ${task.result.user?.displayName}")
                                onResult(true, null)
                            } else {
                                Log.e("FacebookAuth", "Error en login Firebase: ${task.exception?.message}")
                                onResult(false, task.exception?.message)
                            }
                        }
                }
                override fun onCancel() {
                    Log.d("FacebookAuth", "Login cancelado por el usuario")
                    onResult(false, "Login cancelado")
                }
                override fun onError(error: com.facebook.FacebookException) {
                    Log.e("FacebookAuth", "Error en Facebook login: ${error.message}")
                    onResult(false, error.message)
                }
            })

        LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("email", "public_profile"))
    }
}
