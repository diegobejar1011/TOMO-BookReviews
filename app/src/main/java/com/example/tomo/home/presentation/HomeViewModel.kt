package com.example.tomo.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.home.data.repository.HomeRepository
import com.example.tomo.home.data.model.Review
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(
    private val tokenManager: TokenManager
): ViewModel() {

    init {
        Log.d("HOME_VW", "init: Inicialización de la clase ")
    }

    private val repository = HomeRepository(tokenManager)

    private var _reviews = MutableLiveData<List<Review>>()
    val reviews : LiveData<List<Review>> = _reviews

    private var _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    suspend fun onEnter(){
            val result = repository.getReviews()
            result.onSuccess {
                data ->
                if(data.reviews.isNotEmpty()) {
                    _reviews.value = data.reviews
                    _error.value = ""
                } else {
                    _error.value = data.message
                }
            }.onFailure { exception ->
                _error.value = exception.message ?: "Error desconocido"
            }
    }

    @SuppressLint("NewApi")
    fun getBitmapFromPath(context: Context, imagePath: String): Bitmap? {
        val savedImageFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages/IMG_20.jpg")
        Log.d("File Check", "Archivo guardado: ${savedImageFile.exists()}")

        return try {
            val uri = Uri.parse(imagePath)
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}