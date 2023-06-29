package com.example.letterboxf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.model.apiModels.search.SearchPersonResult
import com.example.letterboxf.model.firebase.FirebaseUserModel
import com.example.letterboxf.repository.SearchNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchNetworkRepository: SearchNetworkRepository) : ViewModel(){

    private var _movies = MutableLiveData<ArrayList<PopularMoviesResult>>()
    val movies : LiveData<ArrayList<PopularMoviesResult>>
        get() = _movies

    private var _people = MutableLiveData<ArrayList<SearchPersonResult>>()
    val people : LiveData<ArrayList<SearchPersonResult>>
        get() = _people

    private var _users = MutableLiveData<ArrayList<FirebaseUserModel>>()
    val users : LiveData<ArrayList<FirebaseUserModel>>
        get() = _users

    fun getMovies(query : String){
        viewModelScope.launch {
           _movies.value = searchNetworkRepository.getMovies(query)
        }
    }

    fun getPeople(query : String){
        viewModelScope.launch {
            _people.value = searchNetworkRepository.getPeople(query)
        }
    }

    fun getUsers(query : String){
        viewModelScope.launch {
            _users.value = searchNetworkRepository.getUsers(query)
        }
    }
}