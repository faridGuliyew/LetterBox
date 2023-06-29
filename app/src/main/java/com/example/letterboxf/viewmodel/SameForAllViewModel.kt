package com.example.letterboxf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.credits.MovieCreditsResponse
import com.example.letterboxf.model.apiModels.people.detail.PersonDetailResponse
import com.example.letterboxf.model.apiModels.people.movies.PersonMoviesResponse
import com.example.letterboxf.model.apiModels.reviews.ReviewsResult
import com.example.letterboxf.repository.SameForAllNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SameForAllViewModel @Inject constructor(private val sfaNetworkRepository: SameForAllNetworkRepository) : ViewModel(){

    private var _movieDetails = MutableLiveData<MovieDetailsResponse>()
    val movieDetails : LiveData<MovieDetailsResponse>
        get() = _movieDetails

    private var _movieCredits = MutableLiveData<MovieCreditsResponse>()
    val movieCredits : LiveData<MovieCreditsResponse>
        get() = _movieCredits

    private var _movieReviews = MutableLiveData<ArrayList<ReviewsResult>>()
    val movieReviews : LiveData<ArrayList<ReviewsResult>>
        get() = _movieReviews

    private var _actorDetails = MutableLiveData<PersonDetailResponse>()
    val actorDetails : LiveData<PersonDetailResponse>
        get() = _actorDetails

    private var _actorCredits = MutableLiveData<PersonMoviesResponse>()
    val actorCredits : LiveData<PersonMoviesResponse>
        get() = _actorCredits

    fun getMovieDetails(movieId : String){
        viewModelScope.launch {
            _movieDetails.value = sfaNetworkRepository.getMovieDetails(movieId)
        }
    }
    fun getMovieCredits(movieId: String){
        viewModelScope.launch{
            _movieCredits.value = sfaNetworkRepository.getMovieCredits(movieId)
        }
    }

    fun getMovieReviews(movieId : String){
        viewModelScope.launch{
            _movieReviews.value = sfaNetworkRepository.getMovieReviews(movieId)
        }
    }
    fun getActorDetails(actorId : String){
        viewModelScope.launch {
            _actorDetails.value = sfaNetworkRepository.getActorDetails(actorId)
        }
    }
    fun getPersonCredits(actorId: String){
        viewModelScope.launch {
            _actorCredits.value = sfaNetworkRepository.getPersonCredits(actorId)
        }
    }
}