package com.example.chatapp.firestore.dao

import com.example.chatapp.firestore.model.Category
import com.example.chatapp.firestore.model.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object RoomsDao {
    private fun getRoomsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(Category.ROOMS_COLLECTION)
    }

    fun createRoom(
        title: String,
        desc: String,
        ownerId: String,
        categoryId: Int,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val documentReference = getRoomsCollection().document()
        documentReference.set(
            Room(
                id = documentReference.id,
                title = title,
                description = desc,
                ownerId = ownerId,
                categoryId = categoryId
            )
        ).addOnCompleteListener(onCompleteListener)
    }
}