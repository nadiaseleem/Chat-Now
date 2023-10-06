package com.example.chatapp.util

data class Message(
    var message: String? = null,
    var posName: String? = null,
    var posAction: OnDialogActionClickListener? = null,
    var negName: String? = null,
    var negAction: OnDialogActionClickListener? = null,
    var isCancelable: Boolean = true
)

fun interface OnDialogActionClickListener {
    fun onDialogActionClick()
}
