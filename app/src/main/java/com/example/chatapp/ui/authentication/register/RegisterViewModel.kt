package com.example.chatapp.ui.authentication.register

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

class RegisterViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirmation = MutableLiveData<String>()

    val usernameError = MutableLiveData<String?>()
    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmationError = MutableLiveData<String?>()

    val shouldClearFocus = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val messageLiveData = SingleLiveEvent<Message>()
    val events = SingleLiveEvent<RegisterViewEvent>()
    val hideKeyboard = MutableLiveData<Boolean>()
    private fun insertUserToFirestore(user: User) {
        UsersDao.createUser(user) { task ->
            isLoading
                .postValue(false)
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(message = "User registered successfully", posName = "ok",
                        posAction = {
                            SessionProvider.user = user
                            events.postValue(RegisterViewEvent.NavigateToHome)
                        })
                )
            } else {
                messageLiveData.postValue(
                    Message(
                        task.exception?.localizedMessage
                    )
                )
            }

        }
    }

    fun createAccountWithEmailAndPassword() {
        shouldClearFocus.postValue(true)
        hideKeyboard.postValue(true)
        if (!validateForm()) return
        isLoading.postValue(true)

        firebaseAuth.createUserWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = User(task.result.user?.uid, username = username.value, email.value)
                    insertUserToFirestore(user)

                } else {
                    isLoading
                        .postValue(false)
                    messageLiveData.postValue(
                        Message(
                            task.exception?.localizedMessage
                        )
                    )
                }
            }

    }

    fun onUsernameFocusChange(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val usernameEditText = view as EditText
            val username = usernameEditText.text.toString()
            if (username.isBlank()) {
                usernameError.postValue("please enter username")
            } else {
                usernameError.postValue(null)
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
            } else if (password.length < 8) {
                passwordError.postValue("Minimum 8 characters password")
            } else if (!password.matches(".*[A-Z].*".toRegex())) {
                passwordError.postValue("Must Contain 1 Upper-cae Character")
            } else if (!password.matches(".*[a-z].*".toRegex())) {
                passwordError.postValue("Must Contain 1 Lower-cae Character")
            } else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
                passwordError.postValue("Must Contain 1 Special Character (@#\$%^&+=)")
            } else if (!password.matches(".*[0-9].*".toRegex())) {
                passwordError.postValue("Must Contain 1 number")
            } else {
                passwordError.postValue(null)
            }
        }
    }

    fun onPasswordConfirmationFocusChange(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val passwordConfirmationEditText = view as EditText
            val passwordConfirmation = passwordConfirmationEditText.text.toString()
            if (passwordConfirmation.isBlank()) {
                passwordConfirmationError.postValue("please reenter password")
            } else if (passwordConfirmation != password.value) {
                passwordConfirmationError.postValue("password doesn't match")
            } else {
                passwordConfirmationError.postValue(null)
            }
        }
    }

    fun onLoginClicked() {
        events.postValue(RegisterViewEvent.NavigateToLogin)
    }


    private fun validateForm(): Boolean {
        var valid = true
        if (username.value.isNullOrBlank()) {
            valid = false
            usernameError.postValue("please enter username")
        } else {
            usernameError.postValue(null)
        }
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
        } else if (password.value?.length!! < 8) {
            valid = false
            passwordError.postValue("Minimum 8 characters password")
        } else if (!password.value!!.matches(".*[A-Z].*".toRegex())) {
            valid = false
            passwordError.postValue("Must Contain 1 Upper-cae Character")
        } else if (!password.value!!.matches(".*[a-z].*".toRegex())) {
            valid = false
            passwordError.postValue("Must Contain 1 Lower-cae Character")
        } else if (!password.value!!.matches(".*[@#\$%^&+=].*".toRegex())) {
            valid = false
            passwordError.postValue("Must Contain 1 Special Character (@#\$%^&+=)")
        } else if (!password.value!!.matches(".*[0-9].*".toRegex())) {
            valid = false
            passwordError.postValue("Must Contain 1 number")
        } else {
            passwordError.postValue(null)
        }
        if (passwordConfirmation.value.isNullOrBlank()) {
            valid = false
            passwordConfirmationError.postValue("please reenter password")
        } else if (passwordConfirmation.value != password.value) {
            valid = false
            passwordConfirmationError.postValue("password doesn't match")
        } else {
            passwordConfirmationError.postValue(null)
        }

        return valid
    }

    fun onOutsideClick() {
        hideKeyboard.postValue(true)
    }

}
