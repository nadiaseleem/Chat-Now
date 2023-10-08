package com.example.chatapp.ui.authentication.login

import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.dao.UsersDao
import com.example.chatapp.firestore.model.User
import com.example.chatapp.util.Message
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val email = MutableLiveData<String>("nadia@gmail.com")
    val password = MutableLiveData<String>("Nadia@123")

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val shouldClearFocus = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val messageLiveData = SingleLiveEvent<Message>()
    val events = SingleLiveEvent<LoginViewEvents>()
    val hideKeyboard = MutableLiveData<Boolean>()
    fun signInWithEmailAndPassword() {
        shouldClearFocus.postValue(true)
        hideKeyboard.postValue(true)
        if (!validateForm()) return
        isLoading.postValue(true)

        firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserFromFirestore(task.result.user?.uid)
                } else {
                    isLoading.postValue(false)
                    messageLiveData.postValue(
                        Message(
                            task.exception?.localizedMessage,
                            posName = "ok"
                        )
                    )
                }

            }
    }

    private fun getUserFromFirestore(uid: String?) {
        UsersDao.getUser(uid ?: "") { task ->
            isLoading.postValue(false)
            if (task.isSuccessful) {
                val user = task.result.toObject(User::class.java)
                SessionProvider.user = user
                events.postValue(LoginViewEvents.NavigateToHome)
            } else {
                messageLiveData.postValue(Message(task.exception?.localizedMessage, posName = "ok"))
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

    fun onRegisterClicked() {
        events.postValue(LoginViewEvents.NavigateToRegister)
    }

    fun onOutsideClick() {
        hideKeyboard.postValue(true)
    }
}