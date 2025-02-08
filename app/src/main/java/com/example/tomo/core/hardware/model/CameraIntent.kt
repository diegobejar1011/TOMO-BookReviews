package com.example.tomo.core.hardware.model

import android.content.Context
import android.net.Uri

sealed class CameraIntent {
    data class OnPermissionGrantedWith(val compositionContext: Context) : CameraIntent()
    data object OnPermissionDenied : CameraIntent()
    data class OnImageSavedWith(val compositionContext: Context) : CameraIntent()
    data object OnImageSavingCanceled : CameraIntent()
}
