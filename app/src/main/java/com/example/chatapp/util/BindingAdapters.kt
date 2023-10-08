package com.example.chatapp.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:Error")
fun setTextInputLayoutError(textInputLayout: TextInputLayout, error: String?) {
    textInputLayout.error = error
    textInputLayout.errorIconDrawable = null
}

@BindingAdapter("app:clearFocusOnCondition")
fun clearFocusOnCondition(view: View, condition: Boolean) {
    if (condition) {
        view.clearFocus()
    }
}

@BindingAdapter("app:onFocusChange")
fun setOnFocusChangeListener(
    editText: TextInputEditText,
    onFocusChangeListener: View.OnFocusChangeListener?
) {
    @BindingAdapter("app:Error")
    fun setTextInputLayoutError(textInputLayout: TextInputLayout, error: String?) {
        textInputLayout.error = error
        textInputLayout.errorIconDrawable = null
    }

    @BindingAdapter("app:clearFocusOnCondition")
    fun clearFocusOnCondition(view: View, condition: Boolean) {
        if (condition) {
            view.clearFocus()
        }
    }

    @BindingAdapter("app:onFocusChange")
    fun setOnFocusChangeListener(
        editText: TextInputEditText,
        onFocusChangeListener: View.OnFocusChangeListener?
    ) {
        // Remove the existing onFocusChangeListener if any
        editText.onFocusChangeListener = null

        // Set the new onFocusChangeListener if provided
        if (onFocusChangeListener != null) {
            editText.onFocusChangeListener = onFocusChangeListener
        }
    }

}
