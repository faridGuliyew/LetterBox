package com.example.letterboxf.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.firebase.FirebaseResponse
import com.example.letterboxf.model.firebase.FirebaseReviewModel
import com.example.letterboxf.repository.FirebaseDatabaseRepository
import com.example.letterboxf.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val firebaseDatabaseRepository: FirebaseDatabaseRepository): ViewModel() {

    val response = SingleLiveData<FirebaseResponse>()

    private val _reviews = MutableLiveData<ArrayList<FirebaseReviewModel>>()
    val reviews : LiveData<ArrayList<FirebaseReviewModel>>
        get() = _reviews

    fun addReview(id : String,title : String,url : String, content : String, rating : Double){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.addReview(id, title,url,content, rating)
        }
    }

    fun getUserReview(uid : String?, filmId : String?){
        viewModelScope.launch {
            _reviews.value = firebaseDatabaseRepository.getUserReviews(uid,filmId)
        }
    }

    fun getFriendsReview(movieId:String){
        viewModelScope.launch {
            _reviews.value = firebaseDatabaseRepository.getFriendReviews(movieId)
        }
    }
}