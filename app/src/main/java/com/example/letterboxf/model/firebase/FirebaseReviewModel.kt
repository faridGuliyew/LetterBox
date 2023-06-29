package com.example.letterboxf.model.firebase

data class FirebaseReviewModel(
    val id : String="0",
    val title : String="Undefined title",
    val url : String = "unknown",
    val content : String = "No content",
    val owner : String = "someone",
    val owner_uid : String = "unknown",
    val rating : Double = 0.0)
