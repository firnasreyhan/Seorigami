package com.project.seorigami.util

import android.content.Context

object Utils {

    fun reformatToken(context: Context) : String {
        return "Bearer ${Prefs(context).jwt.toString()}"
    }

    fun changePrice(text: String): String {
        var formatPrice = ""

        for (i in 1..text.length) {
            if (i % 3 == 0 && i < text.length) {
                formatPrice = "," + text[text.length - i] + formatPrice
            } else {
                formatPrice = text[text.length - i] + formatPrice
            }
        }

        return "Rp " + formatPrice
    }
}