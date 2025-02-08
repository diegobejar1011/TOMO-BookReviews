package com.example.tomo.core.hardware.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tomo.core.hardware.model.CameraIntent
import com.example.tomo.core.hardware.model.CameraState

@Composable
fun CameraScreen(modifier: Modifier = Modifier, viewModel: CameraViewModel) {
    // collecting the flow from the view model as a state allows our ViewModel and View
    // to be in sync with each other.
    val viewState: CameraState by viewModel.viewStateFlow.collectAsState()

    val currentContext = LocalContext.current

    // launches camera
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            viewModel.onReceive(CameraIntent.OnImageSavedWith(currentContext))
        } else {
            // handle image saving error or cancellation
            viewModel.onReceive(CameraIntent.OnImageSavingCanceled)
        }
    }

    // launches camera permissions
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            viewModel.onReceive(CameraIntent.OnPermissionGrantedWith(currentContext))
        } else {
            // handle permission denied such as:
            viewModel.onReceive(CameraIntent.OnPermissionDenied)
        }
    }

    // this ensures that the camera is launched only once when the url of the temp file changes
    LaunchedEffect(key1 = viewState.tempFileUrl) {
        viewState.tempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }


    Column(modifier = Modifier.fillMaxSize().padding(20.dp)
        .verticalScroll(rememberScrollState()).then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Button(onClick = {
                if (viewState.tempFileUrl == null) {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            } else {
                cameraLauncher.launch(viewState.tempFileUrl!!)
            }
            }) {
                Text(text = "Take a photo")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Selected Pictures")
        if(viewState.selectedPicture !=  null)  {
            Image(modifier = Modifier.padding(6.dp)
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp)),
                bitmap = viewState.selectedPicture!!,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}