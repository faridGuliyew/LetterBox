package com.example.letterboxf.model.firebase

data class MovieActivityModel(val isWatched : Boolean= false,
                              val isLiked : Boolean= false,
                              val isWatchListed : Boolean= false,
                              val rating : Double = 0.0
)
