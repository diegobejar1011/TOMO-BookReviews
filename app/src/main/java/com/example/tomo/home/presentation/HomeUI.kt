package com.example.tomo.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.tomo.R
import com.example.tomo.home.data.model.Review
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(homeViewModel : HomeViewModel, navigateToAdd: () -> Unit){

    val error:String by homeViewModel.error.observeAsState("No hay reseñas ¡Agrega algunas!")
    val reviews: List<Review> by homeViewModel.reviews.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        homeViewModel.onEnter()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ){
        Header()

        ContentCards(error, reviews, navigateToAdd)

    }
}

@Composable
fun Header(){
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
            modifier = Modifier.size(100.dp).padding(horizontal = 10.dp)
        )
        Text(
                text= "Home",
                color = Color(0xFFF6F6F6),
                fontSize = 40.sp,
                modifier = Modifier.padding(20.dp)
            )
    }
}

@Composable
fun ContentCards(error: String, reviews:List<Review>, navigateToAdd: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()) {

        Column(modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
        ) {
            Cards(reviews)

            Text(text= error,
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                fontSize = 20.sp,
                color = Color(0xFF4A4A4A),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Button( onClick = { navigateToAdd() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000)
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 10.dp, vertical = 40.dp).size(80.dp),

        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                tint = Color(0xFFF6F6F6),
                modifier = Modifier.size(30.dp).shadow(60.dp)
            )
        }
    }
}

@Composable
fun Cards(reviews:List<Review>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 20.dp)
    ){
        for (review in reviews){
            Card(review)
        }
    }
}

@Composable
fun Card(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), ambientColor = Color.Black.copy(alpha = 0.5f)),
        verticalArrangement = Arrangement.Center,
    ){
        Text(   text = review.book_title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
        Text(   text = review.book_author,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Rating(review.rating)

        Text(
            text = review.description,
            modifier = Modifier.padding(20.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun Rating(rating: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center
    ){
        for (i in 1..rating) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "",
                tint = Color(0xFFF19E39)
            )
        }
    }
}
