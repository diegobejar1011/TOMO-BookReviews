package com.example.tomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomo.core.hardware.view.CameraScreen
import com.example.tomo.core.hardware.view.CameraViewModel
import com.example.tomo.core.navigation.NavigationWrapper
import com.example.tomo.login.presentation.LoginScreen
import com.example.tomo.ui.theme.TOMOTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TOMOTheme {
                NavigationWrapper()
            }
        }
    }
}

