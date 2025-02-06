package com.example.tomo.add.presentation

import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.add.data.model.CreateReviewRequest
import com.example.tomo.add.data.repository.AddRepository
import com.example.tomo.core.storage.TokenManager
import kotlinx.coroutines.launch

class AddViewModel(
    private val tokenManager: TokenManager
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

}