package com.example.tomo.add.data.model

data class CreateReviewRequest (
    val book_title: String,
    val book_author: String,
    val rating: Int,
    val description: String
)