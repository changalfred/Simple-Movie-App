package com.example.simplemovieapp.utilities

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

object Formatters {

    fun roundToNearestTenth(rating: Double): Double {
        return (rating * 10.0).roundToInt() / 10.0
    }

    // TODO: Long bug: Must parse into received format before wanted format. Spent a lot of time
    // parsing directly into wanted format.
    fun formatDate(date: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // First parse into received format.
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(date, format)

            // Second parse into wanted format.
            val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
            localDate.format(formatter)
        } else {
            date
        }
    }

    // Always return currency value as a long and not a string.
    fun formatWithCommas(arg: Int): String {
        if (arg.toString().length >= 3) {
            val stringBuilder = StringBuilder(arg.toString())
            var i = arg.toString().length - 3

            while (i >= 1) {
                stringBuilder.insert(i, ",")
                i -= 3
            }

            return stringBuilder.toString()
        }

        return "0"
    }

    fun buildImageUri(dims: String, path: String) =
        StringBuilder(TMDB_IMAGE_BASE_URL)
            .append(dims)
            .append(SLASH)
            .append(path)
            .toString()

}