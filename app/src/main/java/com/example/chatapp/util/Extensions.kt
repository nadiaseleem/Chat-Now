package com.example.chatapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun View.hideKeyboard(activity: AppCompatActivity?) {
    val inputMethodManager =
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}


fun Fragment.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: OnDialogActionClickListener? = null,
    negActionName: String? = null,
    negAction: OnDialogActionClickListener? = null,
    isCancelable: Boolean = true
): AlertDialog {

    val alertDialogBuilder = AlertDialog.Builder(requireContext())
    alertDialogBuilder.setMessage(message)
    if (posActionName != null)
        alertDialogBuilder.setPositiveButton(posActionName) { dialogInterface, id ->
            dialogInterface.dismiss()
            posAction?.onDialogActionClick()
        }
    if (negActionName != null)
        alertDialogBuilder.setNegativeButton(negActionName) { dialogInterface, id ->
            dialogInterface.dismiss()
            negAction?.onDialogActionClick()
        }
    alertDialogBuilder.setCancelable(isCancelable)
    return alertDialogBuilder.show()
}


fun Activity.showMessage(
    message: String,
    posActionName: String? = null,
    posAction: OnDialogActionClickListener? = null,
    negActionName: String? = null,
    negAction: OnDialogActionClickListener? = null,
    isCancelable: Boolean = true
): AlertDialog {

    val alertDialogBuilder = AlertDialog.Builder(this)
    alertDialogBuilder.setMessage(message)
    if (posActionName != null)
        alertDialogBuilder.setPositiveButton(posActionName) { dialogInterface, id ->
            dialogInterface.dismiss()
            posAction?.onDialogActionClick()
        }
    if (negActionName != null)
        alertDialogBuilder.setNegativeButton(negActionName) { dialogInterface, id ->
            dialogInterface.dismiss()
            negAction?.onDialogActionClick()
        }
    alertDialogBuilder.setCancelable(isCancelable)
    return alertDialogBuilder.show()
}

fun Fragment.hideKeyboard() {
    view?.hideKeyboard(activity as AppCompatActivity?)
}

fun Activity.hideKeyboard() {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = currentFocus
    if (currentFocusView != null) {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
