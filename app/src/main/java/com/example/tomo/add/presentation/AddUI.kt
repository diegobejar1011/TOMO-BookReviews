package com.example.tomo.add.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Column( modifier = Modifier
        .fillMaxSize()
    ) {
        Header(navigateToHome)
        ContentInputs(error, title, author, rating, description, addViewModel, tokenManager)
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
fun ContentInputs(error: String, title: String, author: String, rating: Int, description: String, addViewModel: AddViewModel, tokenManager: TokenManager){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
                val createReviewRequest = CreateReviewRequest(title, author, rating, description)
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
fun Rating(counter: Int, addViewModel: AddViewModel) {

    val starsState = remember { mutableStateListOf(false, false, false, false, false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ){

        starsState.forEachIndexed { index, isSelected ->
            IconButton(
                onClick = {
                    starsState[index] = !starsState[index]
                    val newRating = starsState.count { it }
                    addViewModel.onChangeRating(newRating)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star $index",
                    tint = if (isSelected) Color(0xFFFFD700) else Color(0xFFB3B2AE),
                    modifier = Modifier.size(50.dp)
                )
            }
        }

    }
}