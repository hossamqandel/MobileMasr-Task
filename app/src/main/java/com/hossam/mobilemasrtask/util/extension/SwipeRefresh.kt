package com.hossam.mobilemasrtask.util.extension

import android.graphics.Color
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hossam.mobilemasrtask.R

fun SwipeRefreshLayout.setProgressColor(color: Int){
    setColorSchemeColors(this.context.getColor(color))
}