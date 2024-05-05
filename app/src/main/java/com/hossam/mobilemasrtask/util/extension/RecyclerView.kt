package com.hossam.mobilemasrtask.util.extension

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addDivider() {
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}