package com.crewup.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.crewup.myapplication.ui.theme.CrewUpTheme
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrewUpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Row(modifier = Modifier.fillMaxSize(),verticalAlignment = Alignment.CenterVertically) {   DebugButton()}


                }
            }
        }
    }
}

fun onClick(){
    println("hola")
    Firebase.analytics.logEvent("log_button_click",null)
}

@Composable
fun DebugButton(){
    Button(
        onClick = { onClick() }

    ) {
        Text("Hola!")
    }
}

