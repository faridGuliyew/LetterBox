package com.example.letterboxf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.repository.PopularNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PopularViewModel @Inject constructor(private val popularNetworkRepository: PopularNetworkRepository) : ViewModel() {

    private var _popularMovies = MutableLiveData<ArrayList<PopularMoviesResult>>()
    val popularMovies : LiveData<ArrayList<PopularMoviesResult>>
        get() = _popularMovies

    private var _reviews = MutableLiveData<ArrayList<com.example.letterboxf.model.apiModels.reviews.ReviewsResult>>()
    val reviews : LiveData<ArrayList<com.example.letterboxf.model.apiModels.reviews.ReviewsResult>>
        get() = _reviews

    private var _popularPeople = MutableLiveData<ArrayList<com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult>>()
    val popularPeople : LiveData<ArrayList<com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult>>
        get() = _popularPeople

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading


    fun getPopularReviews(page : Int){
        viewModelScope.launch {
            _isLoading.value = true
            //Popular reviews use the same data as in movies (In order to display the photo as well)
            _popularMovies.value = popularNetworkRepository.getPopularReviews(page)
            _isLoading.value = false
        }
    }

    fun getPopularPeople(page: Int){
        viewModelScope.launch {
            _isLoading.value = true
            _popularPeople.value = popularNetworkRepository.getPopularPeople(page)
            _isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        _isLoading.value = false
    }
}