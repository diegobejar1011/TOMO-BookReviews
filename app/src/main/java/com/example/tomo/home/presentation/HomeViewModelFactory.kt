package com.example.tomo.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.register.presentation.RegisterViewModel

class HomeViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create (modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}