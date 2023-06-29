package com.example.letterboxf.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxf.model.firebase.FirebaseDoubleResponse
import com.example.letterboxf.model.firebase.FirebaseResponse
import com.example.letterboxf.repository.FirebaseDatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileCheckViewModel @Inject constructor(auth : FirebaseAuth, private val firestore: FirebaseFirestore, private val storage : FirebaseStorage) : ViewModel() {

    private var _response = MutableLiveData<FirebaseDoubleResponse>()
    val response : LiveData<FirebaseDoubleResponse>
        get() = _response

    private var _uri = MutableLiveData<Uri?>()
    val uri : LiveData<Uri?>
        get() = _uri

    private val uid = auth.uid

    fun fetchDisplayName() {
        viewModelScope.launch {
            if (uid != null){
                try {
                    val request = firestore.collection("users").document(uid).get().await()
                    val name = request["displayName"].toString()
                    val uid = request["uid"].toString()
                    if (name != "null"){
                        _response.value = FirebaseDoubleResponse(name,uid,true)
                    }
                }catch (e:Exception){
                    _response.value = FirebaseDoubleResponse()
                }
            }else{
                _response.value = FirebaseDoubleResponse()
            }
        }
    }

    fun fetchProfilePhoto() {
        viewModelScope.launch {
            try {
                Log.e("SERIOUS",uid.toString())
                val reference = storage.getReference("pfp/$uid")
                val metadata = reference.metadata.await()
                if (metadata.sizeBytes > 0) {
                    _uri.value = reference.downloadUrl.await()
                } else{
                    Log.e("SERIOUS","Nothing found")
                    _uri.value = null
                }
            }catch (e:Exception){
                Log.e("SERIOUS",e.localizedMessage)
                _uri.value = null
            }
        }
    }
}