package com.example.chatapp.firestore.model

data class Room(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val categoryId: Int? = null
)