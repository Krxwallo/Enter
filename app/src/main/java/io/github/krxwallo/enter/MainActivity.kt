package io.github.krxwallo.enter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.krxwallo.enter.net.APIClient
import io.github.krxwallo.enter.ui.page.HomePage
import io.github.krxwallo.enter.ui.theme.EnterTheme

const val TAG = "EnterApp"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Hello world!")
        preferences = getSharedPreferences("Enter_UserData", MODE_PRIVATE)
        APIClient.ip = getCachedIp() ?: APIClient.ip
        setContent {
            EnterTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(Modifier.fillMaxSize())
                }
            }
        }
    }
}