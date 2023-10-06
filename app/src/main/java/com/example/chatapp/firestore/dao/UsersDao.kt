package com.example.chatapp.firestore.dao

import com.example.chatapp.firestore.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object UsersDao {

    fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(User.USERS_COLLECTION)
    }

    fun createUser(user: User, onCompleteListener: OnCompleteListener<Void>) {
        getUsersCollection().document(user.id ?: "").set(user)
            .addOnCompleteListener(onCompleteListener)
    }

    fun getUser(uid: String, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
        getUsersCollection().document(uid).get().addOnCompleteListener(onCompleteListener)
    }
}