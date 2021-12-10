package io.github.pengdst.salescashier.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context?.longToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context?.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String?) {
    context.longToast(message)
}

fun Fragment.shortToast(message: String?) {
    context.shortToast(message)
}

fun Activity.longToast(message: String?) {
    applicationContext.longToast(message)
}

fun Activity.shortToast(message: String?) {
    applicationContext.shortToast(message)
}