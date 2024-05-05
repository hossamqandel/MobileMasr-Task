package com.hossam.mobilemasrtask.util.extension

import android.view.View

fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

inline fun View.onClick(crossinline block: () -> Unit) {
    this.setOnClickListener { block.invoke() }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
