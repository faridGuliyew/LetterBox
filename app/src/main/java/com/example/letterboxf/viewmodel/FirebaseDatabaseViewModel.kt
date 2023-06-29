package com.example.letterboxf.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.firebase.FirebaseFriendModel
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.model.firebase.FirebaseResponse
import com.example.letterboxf.model.firebase.FirebaseUserModel
import com.example.letterboxf.model.firebase.MovieActivityModel
import com.example.letterboxf.repository.FirebaseDatabaseRepository
import com.example.letterboxf.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseDatabaseViewModel @Inject constructor(private val firebaseDatabaseRepository: FirebaseDatabaseRepository) : ViewModel() {

    val response = SingleLiveData<FirebaseResponse>()
    private val _friendsResponse = MutableLiveData<ArrayList<FirebaseUserModel>>()
    val friendsResponse : LiveData<ArrayList<FirebaseUserModel>>
        get() = _friendsResponse
    val addToFriendsResponse = SingleLiveData<FirebaseResponse>()
    val removeFromFriendsResponse = SingleLiveData<FirebaseResponse>()
    //Since liked and watched movies are at the same fragment, i had to separate variables
    private val _likedMovies = MutableLiveData<ArrayList<FirebaseMovieModel>>()
    val likedMovies : LiveData<ArrayList<FirebaseMovieModel>>
        get() = _likedMovies

    private val _movies = MutableLiveData<ArrayList<FirebaseMovieModel>>()
    val movies : LiveData<ArrayList<FirebaseMovieModel>>
        get() = _movies

    private val _movieActivity = MutableLiveData<MovieActivityModel>()
    val movieActivity : LiveData<MovieActivityModel>
        get() = _movieActivity

    private val _displayName = MutableLiveData<String>()
    val displayName : LiveData<String>
        get() = _displayName

    private val _uri = MutableLiveData<Uri?>()
    val uri : LiveData<Uri?>
        get() = _uri

    fun getDisplayName(uid:String?){
        viewModelScope.launch {
            _displayName.value = firebaseDatabaseRepository.getDisplayName(uid)
        }
    }

    fun addToWatched(movie : FirebaseMovieModel){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.addToWatched(movie)
        }
    }

    fun getWatched(uid : String?){
        viewModelScope.launch{
            _movies.value = firebaseDatabaseRepository.getAnyCollection("watched",uid)
        }
    }

    fun addToLiked(movie : FirebaseMovieModel){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.addToLiked(movie)
        }
    }

    fun getLiked(uid : String?){
        viewModelScope.launch {
            _likedMovies.value = firebaseDatabaseRepository.getAnyCollection("liked",uid)
        }
    }

    fun addToWatchList(movie : FirebaseMovieModel){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.addToWatchList(movie)
        }
    }

    fun getWatchListed(uid : String?){
        viewModelScope.launch {
            _movies.value = firebaseDatabaseRepository.getAnyCollection("watchListed",uid)
        }
    }

    fun getFriendStatus(uid : String){
        viewModelScope.launch{
            response.value = firebaseDatabaseRepository.getFriendsStatus(uid)
        }
    }

    fun addToFriends(friendUid : String, displayName : String){
        viewModelScope.launch {
            addToFriendsResponse.value = firebaseDatabaseRepository.addToFriends(friendUid,displayName)
        }
    }
    fun removeFromFriends(friendUid: String){
        viewModelScope.launch {
            removeFromFriendsResponse.value = firebaseDatabaseRepository.removeFromFriends(friendUid)
        }
    }

    fun getAllFriends(){
        viewModelScope.launch {
            _friendsResponse.value = firebaseDatabaseRepository.getAllFriends()
        }
    }

    fun rateMovie(movieId : String, rating : Double){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.rateMovie(movieId, rating)
        }
    }
    //this function will return whether it is watched,liked,added to the list or rated
    fun getMovieActivity(movieId : String){
        viewModelScope.launch {
            _movieActivity.value = firebaseDatabaseRepository.getMovieActivity(movieId)
        }
    }

    fun removeFromWatched(movieId : String){
        viewModelScope.launch {
            firebaseDatabaseRepository.removeFromWatched(movieId)
        }
    }
    fun removeFromLiked(movieId : String){
        viewModelScope.launch {
            firebaseDatabaseRepository.removeFromLiked(movieId)
        }
    }
    fun removeFromWatchListed(movieId : String){
        viewModelScope.launch {
        firebaseDatabaseRepository.removeFromWatchListed(movieId)
        }
    }

    fun addProfilePhoto(uri:Uri){
        viewModelScope.launch {
            response.value = firebaseDatabaseRepository.addProfilePhoto(uri)
        }
    }

    fun getProfilePhoto(uid : String?){
        viewModelScope.launch {
            val r = firebaseDatabaseRepository.getProfilePhoto(uid)
            Log.e("SERIOUS",r.toString())
            _uri.value = r
        }
    }
}