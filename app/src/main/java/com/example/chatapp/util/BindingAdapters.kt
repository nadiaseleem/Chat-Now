package com.example.chatapp.util

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("android:layout_marginEnd")
fun setLayoutMarginEnd(view: View, marginEnd: Int) {
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.marginEnd = marginEnd
    view.layoutParams = params
}