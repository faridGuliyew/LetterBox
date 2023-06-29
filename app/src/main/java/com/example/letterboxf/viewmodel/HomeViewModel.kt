package com.example.letterboxf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.model.firebase.FirebaseFriendMovieModel
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.repository.FirebaseDatabaseRepository
import com.example.letterboxf.repository.PopularNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val popularNetworkRepository: PopularNetworkRepository, private val firebaseDatabaseRepository: FirebaseDatabaseRepository) : ViewModel() {

    private val _popularMovies = MutableLiveData<ArrayList<PopularMoviesResult>>()
    val popularMovies : LiveData<ArrayList<PopularMoviesResult>>
        get() = _popularMovies

    private var _friendsMovies = MutableLiveData<ArrayList<FirebaseFriendMovieModel>>()
    val friendsMovies : LiveData<ArrayList<FirebaseFriendMovieModel>>
        get() = _friendsMovies

    fun getPopularMovies(page : Int){
        viewModelScope.launch {
            _popularMovies.value = popularNetworkRepository.getPopularMovies(page)
        }
    }

    fun getFriendMovies(){
        viewModelScope.launch {
            _friendsMovies.value = firebaseDatabaseRepository.getFriendsMovies()
        }
    }
}