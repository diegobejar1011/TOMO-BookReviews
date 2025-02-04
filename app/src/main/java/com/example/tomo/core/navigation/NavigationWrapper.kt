package com.example.tomo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tomo.add.data.datasource.AddService
import com.example.tomo.add.presentation.AddScreen
import com.example.tomo.add.presentation.AddViewModel
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.home.presentation.HomeScreen
import com.example.tomo.home.presentation.HomeViewModel
import com.example.tomo.login.presentation.LoginViewModel
import com.example.tomo.login.presentation.LoginScreen
import com.example.tomo.register.presentation.RegisterScreen
import com.example.tomo.register.presentation.RegisterViewModel


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val tokenManager = TokenManager(LocalContext.current)

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> { LoginScreen(LoginViewModel(tokenManager), navigateToHome = { navController.navigate(Home) }, navigateToRegister = { navController.navigate(Register) })}
        composable<Register> { RegisterScreen(RegisterViewModel()){ navController.navigate(Home)} }
        composable<Home> { HomeScreen(HomeViewModel(tokenManager)){ navController.navigate(Add)} }
        composable<Add> { AddScreen(AddViewModel(tokenManager), navigateToHome = { navController.navigate(Home)}, tokenManager)}
    }

}