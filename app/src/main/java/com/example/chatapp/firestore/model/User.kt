package com.example.chatapp.firestore.model

data class User(val id: String? = null, val username: String? = null, val email: String? = null) {
    companion object {
        const val USERS_COLLECTION = "users"
    }
}