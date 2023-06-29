package com.example.letterboxf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.firebase.FirebaseResponse
import com.example.letterboxf.repository.FirebaseAuthRepository
import com.example.letterboxf.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val firebaseAuthRepository: FirebaseAuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    val firebaseResponse = SingleLiveData<FirebaseResponse>()

    fun login(username:String,password:String){
        viewModelScope.launch {
            _isLoading.value = true
            firebaseResponse.value = firebaseAuthRepository.login(username, password)
            _isLoading.value = false
        }
    }

    fun register(username:String,password:String,displayName:String){
        viewModelScope.launch {
            _isLoading.value = true
            firebaseResponse.value = firebaseAuthRepository.register(username, password,displayName)
            _isLoading.value = false
        }
    }
}