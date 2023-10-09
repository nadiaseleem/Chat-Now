package com.example.chatapp.firestore.model

data class Category(val id: Int, val name: String) {
    companion object {
        fun getAllCategories(): List<Category> {
            return listOf(Category(1, "Sports"), Category(2, "Music"), Category(3, "Movies"))
        }

        const val ROOMS_COLLECTION = "rooms"
    }
}
