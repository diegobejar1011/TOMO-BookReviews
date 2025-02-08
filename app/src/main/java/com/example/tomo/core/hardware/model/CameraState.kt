package com.example.tomo.core.hardware.model

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

data class CameraState (
    val tempFileUrl: Uri? = null,
    val selectedPicture: ImageBitmap? = null,
    val pathImageSave: String? =  null
)