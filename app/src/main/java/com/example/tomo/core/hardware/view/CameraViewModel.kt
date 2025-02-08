package com.example.tomo.core.hardware.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.core.hardware.model.CameraState
import com.example.tomo.core.hardware.model.CameraIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class CameraViewModel(private val coroutineContext: CoroutineContext): ViewModel() {

    private val _albumViewState: MutableStateFlow<CameraState> = MutableStateFlow(CameraState())

    val viewStateFlow: StateFlow<CameraState>
        get() = _albumViewState

    @SuppressLint("NewApi")
    fun onReceive(intent: CameraIntent) = viewModelScope.launch(coroutineContext) {
        when(intent) {

            is CameraIntent.OnPermissionGrantedWith -> {
                Log.d("Permission Camera", "Success: Permissions on")

                // Create an empty image file in the app's cache directory
                val tempFile = File.createTempFile(
                    "temp_image_file_", /* prefix */
                    ".jpg", /* suffix */
                    intent.compositionContext.cacheDir  /* cache directory */
                )

                // Create sandboxed url for this temp file - needed for the camera API
                val uri = FileProvider.getUriForFile(intent.compositionContext,
                    "${BuildConfig.APPLICATION_ID}.provider", /* needs to match the provider information in the manifest */
                    tempFile
                )
                _albumViewState.value = _albumViewState.value.copy(tempFileUrl = uri)
            }

            is CameraIntent.OnPermissionDenied -> {
                println("User did not grant permission to use the camera")
            }


            is CameraIntent.OnImageSavedWith -> {
                val tempImageUrl = _albumViewState.value.tempFileUrl
                if (tempImageUrl != null) {
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, tempImageUrl)

                    _albumViewState.value = _albumViewState.value.copy(tempFileUrl = null, selectedPicture = ImageDecoder.decodeBitmap(source).asImageBitmap())

                }
            }

            is CameraIntent.OnImageSavingCanceled -> {
                _albumViewState.value = _albumViewState.value.copy(tempFileUrl = null)
            }

        }
    }
    // endregion
}