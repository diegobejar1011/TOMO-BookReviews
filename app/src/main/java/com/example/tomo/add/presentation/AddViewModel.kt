package com.example.tomo.add.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.add.data.model.CreateReviewRequest
import com.example.tomo.add.data.repository.AddRepository
import com.example.tomo.core.hardware.model.CameraIntent
import com.example.tomo.core.hardware.model.CameraState
import com.example.tomo.core.hardware.view.BuildConfig
import com.example.tomo.core.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class AddViewModel(
    private val tokenManager: TokenManager,
    private val coroutineContext: CoroutineContext
): ViewModel() {

    init {
        Log.d("ADD_VW", "init: Inicialización de la clase ")
    }

    private val repository = AddRepository(tokenManager)

    private val _title = MutableLiveData<String>()
    var title : LiveData<String> = _title

    private val _author = MutableLiveData<String>()
    var author : LiveData<String> = _author

    private val _rating = MutableLiveData<Int>()
    var rating : LiveData<Int> = _rating

    private val _description = MutableLiveData<String>()
    var description : LiveData<String> = _description

    private val _error = MutableLiveData<String>()
    var error : LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>()
    var success : LiveData<Boolean> = _success

    fun onChangeTitle(title: String) {
        _title.value = title
    }

    fun onChangeAuthor(author: String) {
        _author.value = author
    }

    fun onChangeRating(rating: Int) {
        _rating.value = rating
    }

    fun onChangeDescription(description: String) {
        _description.value = description
    }

    fun onClick(createReviewRequest: CreateReviewRequest) {
        viewModelScope.launch {
            val result = repository.createReview(createReviewRequest)
            result.onSuccess {
                data ->
                if(data.message.isNullOrEmpty()) {
                    _success.value = true
                } else {
                    _error.value = "Faltan campos por llenar"
                }
            }.onFailure {
                exception ->
                _error.value = exception.message ?: "Error desconocido"
            }
        }
    }

    private val _cameraState: MutableStateFlow<CameraState> = MutableStateFlow(CameraState())

    val viewStateFlow: StateFlow<CameraState>
        get() = _cameraState

    @SuppressLint("NewApi")
    fun onReceive(intent: CameraIntent) = viewModelScope.launch(coroutineContext) {
        when(intent) {

            is CameraIntent.OnPermissionGrantedWith -> {
                Log.d("Permission Camera", "Success: Permissions on")

                val tempFile = File.createTempFile(
                    "temp_image_file_",
                    ".jpg",
                    intent.compositionContext.cacheDir
                )

                // Verificar si el archivo se creó correctamente
                Log.d("CAMERA", "Archivo temporal creado: ${tempFile.exists()}")

                val uri = FileProvider.getUriForFile(intent.compositionContext,
                    "${BuildConfig.APPLICATION_ID}.provider",
                    tempFile
                )

                _cameraState.value = _cameraState.value.copy(tempFileUrl = uri)
            }


            is CameraIntent.OnPermissionDenied -> {
                println("User did not grant permission to use the camera")
            }


            is CameraIntent.OnImageSavedWith -> {
                Log.d("CAMERA", "vamoooooooos bien")

                val tempImageUrl = _cameraState.value.tempFileUrl
                if (tempImageUrl != null) {
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, tempImageUrl)

                    val context = intent.compositionContext

                    Log.d("CAMERA", "trata de entraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar")

                    val storageDir = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "MyAppImages"
                    )
                    Log.d("CAMERA", "parece que si ")

                    if (!storageDir.exists()) {
                        Log.d("CAMERA", "lo creooooooooooooooooooooooooooooooooooooo bien")

                        storageDir.mkdirs()
                    }

                    Log.d("CAMERA", "pasooooooooooooo di que si ")

                    // Genera un nombre único para la imagen utilizando el timestamp
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                        Date()
                    )
                    val imageFileName = "IMG_${timeStamp}.jpg"
                    val imageFile = File(storageDir, imageFileName)

                    try {
                        context.contentResolver.openInputStream(tempImageUrl)?.use { inputStream ->
                            FileOutputStream(imageFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }

                        // Ahora guardamos la imagen con una URI única
                        val savedImageUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            imageFile
                        )

                        // Actualiza el estado con la nueva URI
                        _cameraState.value = _cameraState.value.copy(
                            tempFileUrl = null,
                            selectedPicture = ImageDecoder.decodeBitmap(source).asImageBitmap(),
                            pathImageSave = savedImageUri.toString()
                        )



                        Log.d("CAMERA", "onReceive: Imagen guardada en $savedImageUri")
                    } catch (e: Exception) {
                        Log.e("CAMERA", "Error al guardar la imagen: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }



            is CameraIntent.OnImageSavingCanceled -> {
                _cameraState.value = _cameraState.value.copy(tempFileUrl = null)
            }
        }
    }




}