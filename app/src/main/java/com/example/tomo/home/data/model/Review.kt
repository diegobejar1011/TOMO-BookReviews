package com.example.tomo.home.data.model

data class Review (
    val id: Int,
    val id_user: Int,
    val book_title: String,
    val book_author: String,
    val rating: Int,
    val description: String
)