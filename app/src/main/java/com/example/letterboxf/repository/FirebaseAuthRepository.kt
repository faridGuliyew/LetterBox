package com.example.letterboxf.repository

import com.example.letterboxf.model.firebase.FirebaseResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val firebaseAuth : FirebaseAuth, private val firestore : FirebaseFirestore) {

    suspend fun login(username:String,password:String) : FirebaseResponse {
        var firebaseResponse = FirebaseResponse("Something went wrong",false)
        return withContext(Dispatchers.IO){
            try {
                firebaseAuth.signInWithEmailAndPassword(username,password).addOnSuccessListener {
                    firebaseResponse = FirebaseResponse("Logged in successfully!",true)
                }.await()
            }catch (e:Exception){
                firebaseResponse = FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
            firebaseResponse
        }
    }

    suspend fun register(username:String,password:String,displayName : String) : FirebaseResponse {
        var firebaseResponse = FirebaseResponse("Something went wrong",false)
        return withContext(Dispatchers.IO){
            try {
                val request = firebaseAuth.createUserWithEmailAndPassword(username,password).await()
                val uid = request.user!!.uid
                firestore.collection("users").document(uid).set(hashMapOf("displayName" to displayName,"email" to username, "uid" to uid)).addOnSuccessListener {
                    firebaseResponse = FirebaseResponse("Registered successfully!",true)
                }.await()
            }catch (e:Exception){
                firebaseResponse = FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
            firebaseResponse
        }
    }
}