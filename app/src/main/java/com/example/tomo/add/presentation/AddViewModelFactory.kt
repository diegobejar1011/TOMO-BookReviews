package com.example.tomo.add.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.register.presentation.RegisterViewModel

class AddViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create (modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}