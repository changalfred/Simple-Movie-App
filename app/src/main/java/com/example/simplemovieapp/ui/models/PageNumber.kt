package com.example.simplemovieapp.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PageNumber(
    val pageNumber: Int
) : Parcelable
