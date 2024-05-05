package com.hossam.mobilemasrtask.util.extension

import android.app.Activity
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hossam.mobilemasrtask.R


inline fun <reified T : Activity> Activity.startActivity() {
    Intent(this, T::class.java).also {
        startActivity(it)
    }
}

fun Activity.hideStatusBars() {
    WindowInsetsControllerCompat(
        window, window.decorView
    ).hide(WindowInsetsCompat.Type.statusBars())
}

fun Activity.showStatusBars() {
    WindowInsetsControllerCompat(
        window, window.decorView
    ).show(WindowInsetsCompat.Type.statusBars())
}

fun Activity.setSystemBarsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}