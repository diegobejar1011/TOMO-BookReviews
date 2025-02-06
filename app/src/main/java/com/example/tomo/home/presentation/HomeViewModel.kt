package com.example.tomo.home.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.home.data.repository.HomeRepository
import com.example.tomo.home.data.model.Review
import kotlinx.coroutines.launch

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
}