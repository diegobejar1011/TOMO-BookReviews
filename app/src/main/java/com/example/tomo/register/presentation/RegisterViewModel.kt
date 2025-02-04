package com.example.tomo.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomo.register.data.model.CreateUserRequest
import com.example.tomo.register.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val repository = RegisterRepository()

    private var _username = MutableLiveData<String>()
    val username : LiveData<String> = _username

    private var _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private var _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private var _error = MutableLiveData<String>("")
    val error : LiveData<String> = _error

    private var _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean> = _success

    fun onChangeUsername (username: String) {
        _username.value = username
    }

    fun onChangeEmail (email: String) {
        _email.value = email
    }

    fun onChangePassword (password: String) {
        _password.value = password
    }

    suspend fun onClick(createUserRequest: CreateUserRequest) {
        viewModelScope.launch {
            val result = repository.createUser(createUserRequest)
            result.onSuccess {
                data ->
                    if(data.message.isNullOrEmpty()) {
                        _success.value = true
                    } else {
                        _error.value = "Faltan campos por llenar"
                    }
            }.onFailure {
                exception -> _error.value = exception.message ?: "Error desconocido"
            }
        }
    }


}