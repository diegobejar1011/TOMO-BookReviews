package com.example.tomo.add.presentation

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.tomo.R
import com.example.tomo.add.data.model.CreateReviewRequest
import com.example.tomo.core.hardware.model.CameraIntent
import com.example.tomo.core.hardware.model.CameraState
import com.example.tomo.core.navigation.Add
import com.example.tomo.core.storage.TokenManager
import kotlinx.coroutines.launch

@Composable
fun AddScreen(addViewModel: AddViewModel, navigateToHome: () -> Unit, tokenManager: TokenManager) {

    val error by addViewModel.error.observeAsState("")
    val success by addViewModel.success.observeAsState(false)

    val title by addViewModel.title.observeAsState("")
    val author by addViewModel.author.observeAsState("")
    val rating by addViewModel.rating.observeAsState(0)
    val description by addViewModel.description.observeAsState("")

    if(success) {
        navigateToHome()
    }

    val viewState: CameraState by addViewModel.viewStateFlow.collectAsState()

    val currentContext = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            addViewModel.onReceive(CameraIntent.OnImageSavedWith(currentContext))
        } else {
            addViewModel.onReceive(CameraIntent.OnImageSavingCanceled)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            addViewModel.onReceive(CameraIntent.OnPermissionGrantedWith(currentContext))
        } else {
            addViewModel.onReceive(CameraIntent.OnPermissionDenied)
        }
    }



    Column( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
        Header(navigateToHome)
        ContentInputs(error, title, author, rating, description, addViewModel, viewState, permissionLauncher, cameraLauncher)
    }
}

@Composable
fun Header(navigateToHome: () -> Unit){
    val logoHome = painterResource(R.drawable.logohome)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF000000)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = logoHome,
            contentDescription = "",
            modifier = Modifier.size(100.dp).padding(horizontal = 10.dp).clickable { navigateToHome() }
        )
        Text(
            text= "Add review",
            color = Color(0xFFF6F6F6),
            fontSize = 40.sp,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun ContentInputs(error: String, title: String, author: String, rating: Int, description: String, addViewModel: AddViewModel, viewState: CameraState, permissionLauncher: ManagedActivityResultLauncher<String, Boolean>, cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (viewState.tempFileUrl == null) {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    } else {
                        cameraLauncher.launch(viewState.tempFileUrl)
                    }
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Take a photo", fontSize = 16.sp)
            }
            if (viewState.selectedPicture != null) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(20.dp),
                    bitmap = viewState.selectedPicture,
                    contentDescription = "Selected Picture",
                    contentScale = ContentScale.Crop
                )
            }
        }


        TextField(
            value = title,
            onValueChange = { addViewModel.onChangeTitle(it)},
            label = { Text("Title", color = Color(0xFF000000))},
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
                .padding(horizontal = 10.dp, vertical = 30.dp),
        )

        TextField(
            value = author,
            onValueChange = { addViewModel.onChangeAuthor(it)},
            label = { Text("Author", color = Color(0xFF000000))},
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
                .padding(horizontal = 10.dp, vertical = 30.dp),
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
        ){
            Text( text = "Rating",
                    fontSize = 16.sp
                )
            Rating(rating, addViewModel)
        }

        TextField(
            value = description,
            onValueChange = { addViewModel.onChangeDescription(it)},
            label = { Text("Review", color = Color(0xFF000000))},
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
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )

        Button(
            onClick = {
                val createReviewRequest = CreateReviewRequest(title, author, rating, description, viewState.pathImageSave)
                addViewModel.onClick(createReviewRequest)
                      },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 30.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Save",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(text= error,
            modifier = Modifier.padding(5.dp),
            fontSize = 15.sp,
            color = Color(0xFF9B111E),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )


    }
}

@Composable
fun Rating(currentRating: Int, addViewModel: AddViewModel) {
    val starsState = remember { mutableStateListOf(false, false, false, false, false) }

    LaunchedEffect(currentRating) {
        starsState.forEachIndexed { index, _ ->
            starsState[index] = index < currentRating
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        starsState.forEachIndexed { index, _ ->
            IconButton(
                onClick = {
                    val newRating = index + 1  // Seleccionar hasta la estrella tocada
                    addViewModel.onChangeRating(newRating)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star ${index + 1}",
                    tint = if (index < currentRating) Color(0xFFFFD700) else Color(0xFFB3B2AE),
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
