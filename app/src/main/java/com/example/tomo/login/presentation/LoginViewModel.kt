package com.example.tomo.login.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tomo.login.data.model.UserValidateRequest
import androidx.lifecycle.viewModelScope
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.login.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val tokenManager: TokenManager
): ViewModel() {

    init {
        Log.d("LOGIN_VW", "init: Inicialización de la clase ")
    }

    private val repository = LoginRepository()

    private var _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private var _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private var _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private var _success = MutableLiveData<Boolean>()
    val success : LiveData<Boolean>  = _success

    fun onChangeEmail(email: String) {
        _email.value = email
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }

    fun onClick(validateRequest: UserValidateRequest) {
        viewModelScope.launch {
            val result = repository.validateUser(validateRequest)
            result.onSuccess { data ->
                if(data.id != null) {
                    if (data.validate) {
                        _success.value = true
                        tokenManager.saveToken(data.token!!, 2)
                    } else {
                        _error.value = "Credenciales incorrectas"
                    }
                } else {
                    _error.value = "Usuario no encontrado"
                }

            }.onFailure { exception ->
                _error.value = exception.message ?: "Error desconocido"
            }
        }
    }

}