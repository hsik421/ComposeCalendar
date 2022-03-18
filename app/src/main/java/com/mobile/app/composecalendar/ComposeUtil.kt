package com.mobile.app.composecalendar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun String.color() : Color {
    if(this.isNotEmpty()){
        return if(this.first().toString() != "#"){
            Color(android.graphics.Color.parseColor("#$this"))
        }else{
            Color(android.graphics.Color.parseColor(this))
        }

    }else{
        throw IllegalArgumentException("Color String Not Found")
    }
}

fun Int.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}