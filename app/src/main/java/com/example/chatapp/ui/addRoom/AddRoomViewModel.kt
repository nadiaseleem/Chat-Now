package com.example.chatapp.ui.addRoom

import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.dao.RoomsDao
import com.example.chatapp.firestore.model.Category
import com.example.chatapp.util.Message

class AddRoomViewModel : ViewModel() {
    val events = SingleLiveEvent<AddRoomViewEvent>()
    val isLoading = MutableLiveData<Boolean>()

    val messageLiveData = SingleLiveEvent<Message>()
    val roomName = MutableLiveData<String>()
    val roomDescription = MutableLiveData<String>()

    val roomNameError = MutableLiveData<String?>()

    val shouldClearFocus = MutableLiveData<Boolean>()
    val hideKeyboard = MutableLiveData<Boolean>()
    private val categories = Category.getAllCategories()
    var selectedCategory = categories[0]


    fun createRoom() {
        shouldClearFocus.postValue(true)
        hideKeyboard.postValue(true)
        if (!validateForm()) return
        isLoading.postValue(true)

        RoomsDao.createRoom(

            roomName.value ?: "",
            roomDescription.value ?: "",
            SessionProvider.user?.id ?: "",
            selectedCategory.id

        ) { task ->
            isLoading.postValue(false)
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "Room created successfully",
                        posName = "ok", posAction = {
                            events.postValue(AddRoomViewEvent.NavigateToHome)

                        }
                    )
                )


            } else {
                messageLiveData.postValue(
                    Message(
                        message = task.exception?.localizedMessage,
                        posName = "try again",
                        posAction = {
                            createRoom()
                        },
                        negName = "ok"
                    )
                )
            }
        }

    }

    fun onRoomNameFocusChange(view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val roomNameEditText = view as EditText
            val password = roomNameEditText.text.toString()
            if (password.isBlank()) {
                roomNameError.postValue("please enter a room name")
            } else {
                roomNameError.postValue(null)
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        if (roomName.value.isNullOrBlank()) {
            valid = false
            roomNameError.postValue("please enter a room name")
        } else {
            roomNameError.postValue(null)
        }
        return valid
    }

    fun onOutsideClick() {
        hideKeyboard.postValue(true)
    }

    fun onCategorySelected(position: Int) {
        selectedCategory = categories[position]
    }
}