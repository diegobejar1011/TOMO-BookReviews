package com.example.tomo.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.tomo.R
import com.example.tomo.login.data.model.UserValidateRequest
import kotlinx.coroutines.launch

// @Preview(showBackground = true)
@Composable
fun LoginScreen(loginModelView: LoginViewModel, navigateToHome: () -> Unit, navigateToRegister: () -> Unit ){
    val logoRegister = painterResource(R.drawable.logoregister)

    val email:String by loginModelView.email.observeAsState("")
    val password:String by loginModelView.password.observeAsState("")
    val error:String by loginModelView.error.observeAsState("")

    val success:Boolean by loginModelView.success.observeAsState(false)

    var isPasswordVisible by remember { mutableStateOf(false) }

    if (success) {
        navigateToHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = logoRegister,
            contentDescription = "",
            modifier = Modifier.size(250.dp)
        )
        TextField(
            value = email,
            onValueChange = { loginModelView.onChangeEmail(it) },
            label = { Text("Email", color = Color(0xFF000000)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF6F6F6),
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedTextColor = Color.Black
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
        )

        TextField(
            value = password,
            onValueChange = { loginModelView.onChangePassword(it) },
            label = { Text("Password", color = Color(0xFF000000)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF6F6F6),
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedTextColor = Color.Black
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible}){
                    Icon(
                        imageVector = if(isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if(isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color(0xFF000000)
                    )
                }
            },
            visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.Center) {
            Text(text= "¿Aún no tienes una cuenta?",
                fontSize = 15.sp,
                color = Color(0xFF000000),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Text(text= "Registrate",
            modifier = Modifier.padding(horizontal = 5.dp).clickable { navigateToRegister() },
            fontSize = 15.sp,
            color = Color(0xFF4A4A4A),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                val userValidateRequest = UserValidateRequest(email, password)
                loginModelView.onClick(userValidateRequest)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Sign in",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(text= error,
            modifier = Modifier.padding(5.dp),
            fontSize = 20.sp,
            color = Color(0xFF9B111E),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

    }
}