package com.example.tomo.core.storage

import android.content.Context
import java.security.SignatureException

class TokenManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private var id: Int = 0

    fun saveToken(token: String, id: Int){
        sharedPreferences.edit().putString("auth_token", token).apply()
        this.id = id
    }

    fun getToken():String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun getId():Int {
        return this.id
    }
}