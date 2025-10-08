package com.crewup.myapplication.auth

import android.app.Activity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FacebookAuth(private val activity: Activity) {

    private val auth = FirebaseAuth.getInstance()
    val callbackManager = CallbackManager.Factory.create()

    fun login(onResult: (Boolean, String?) -> Unit) {
        LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : com.facebook.FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            onResult(task.isSuccessful, task.exception?.message)
                        }
                }
                override fun onCancel() = onResult(false, "Cancelled")
                override fun onError(error: com.facebook.FacebookException) =
                    onResult(false, error.message)
            })
    }
}
