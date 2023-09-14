package com.example.chatapp.home.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }

    }
}