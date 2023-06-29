package com.example.letterboxf.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FirebaseMovieModel(
    val id : String = "13",
    val name : String = "Not defined",
    val url : String = "brokenLink",
    val rating : Double = 0.0,
    val liked : Boolean = false,
) : Parcelable