package com.example.chatapp.home.login

import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.util.ViewError
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val shouldClearFocus = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<ViewError>()

    fun signInWithEmailAndPassword() {
        shouldClearFocus.postValue(true)
        if (!validateForm()) return
        isLoading.postValue(true)

        firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                isLoading.postValue(false)
                if (task.isSuccessful) {
                    errorLiveData.postValue(ViewError(task.result.user?.uid))
                } else {
                    errorLiveData.postValue(ViewError(task.exception?.localizedMessage))
                }

            }
    }

    fun onEmailFocusChange(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val emailEditText = view as EditText
            val email = emailEditText.text.toString()
            if (email.isBlank()) {
                emailError.postValue("please enter email")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError.postValue("please enter a valid email address")
            } else {
                emailError.postValue(null)
            }
        }
    }

    fun onPasswordFocusChange(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val passwordEditText = view as EditText
            val password = passwordEditText.text.toString()
            if (password.isBlank()) {
                passwordError.postValue("please enter password")
            } else {
                passwordError.postValue(null)
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        if (email.value.isNullOrBlank()) {
            valid = false
            emailError.postValue("please enter email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()) {
            valid = false
            emailError.postValue("please enter a valid email address")
        } else {
            emailError.postValue(null)
        }

        if (password.value.isNullOrBlank()) {
            valid = false
            passwordError.postValue("please enter password")
        } else {
            passwordError.postValue(null)
        }


        return valid
    }
}