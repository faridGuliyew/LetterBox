package com.example.letterboxf.repository

import android.net.Uri
import android.util.Log
import com.example.letterboxf.model.firebase.FirebaseFriendModel
import com.example.letterboxf.model.firebase.FirebaseFriendMovieModel
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.model.firebase.FirebaseResponse
import com.example.letterboxf.model.firebase.FirebaseReviewModel
import com.example.letterboxf.model.firebase.FirebaseUserModel
import com.example.letterboxf.model.firebase.MovieActivityModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseDatabaseRepository @Inject constructor(private val firestore : FirebaseFirestore, private val storage : FirebaseStorage, auth : FirebaseAuth) {

    private var uid : String  = auth.uid!!
    private val originalUid = uid
    private val userRef = firestore.collection("users").document(uid)

    suspend fun getDisplayName(_uid : String?) : String{
        var result = "..."
        return withContext(Dispatchers.IO){
            try {
                uid = _uid ?: originalUid
                val request = firestore.collection("users").document(uid).get().await()
                result = request["displayName"].toString()
            }catch (_:Exception){ }
            result
        }
    }

    suspend fun getProfilePhoto(_uid: String?) : Uri?{
        return withContext(Dispatchers.IO){
            uid = _uid?:originalUid
            try {
                val reference = storage.getReference("pfp/$uid")
                val metadata = reference.metadata.await()
                if (metadata.sizeBytes > 0) {
                    return@withContext reference.downloadUrl.await()
                } else {
                    return@withContext null
                }
            }catch (e:Exception){
                return@withContext null
            }
        }
    }

    suspend fun addToWatched(movie : FirebaseMovieModel) : FirebaseResponse{
        var response : FirebaseResponse?=null
        withContext(Dispatchers.IO){
            try {
                userRef.collection("watched").document(movie.id).set(movie).addOnSuccessListener {
                    Log.e("COWBoy from db","Success!!!")
                    response = FirebaseResponse("Movie added to watched list!",true)
                }.addOnFailureListener {
                    response = FirebaseResponse("Something went wrong, try again",false)
                }.await()
                if (response==null){
                    response = FirebaseResponse("Fatal error (502)",false)
                }
            }catch (e : Exception){
                Log.e("COWBOY FROM DB","ERROR 501")
                response = FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
        }
        return response!!
    }

    suspend fun addToLiked(movie : FirebaseMovieModel) : FirebaseResponse{
        var response : FirebaseResponse?=null
        withContext(Dispatchers.IO){
            try {
                //Also add liked to the movie inside watched collection
                userRef.collection("watched").document(movie.id).update(hashMapOf("liked" to true) as Map<String, Any>).await()
                userRef.collection("liked").document(movie.id).set(movie).addOnSuccessListener {
                    response = FirebaseResponse("Movie added to liked successfully!",true)
                }.addOnFailureListener {
                    response = FirebaseResponse("Something went wrong!",false)
                }.await()
                if (response==null){
                    response = FirebaseResponse("Fatal error (502)",false)
                }
            }catch (e : Exception){
                response = FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
        }
        return response!!
    }

    suspend fun addToWatchList(movie : FirebaseMovieModel) : FirebaseResponse{
        var response : FirebaseResponse?=null
        withContext(Dispatchers.IO){
            try {
                userRef.collection("watchListed").document(movie.id).set(movie).addOnSuccessListener {
                    response = FirebaseResponse("Movie added to watchlist successfully!",true)
                }.addOnFailureListener {
                    response = FirebaseResponse("Something went wrong!",false)
                }.await()
                if (response==null){
                    response = FirebaseResponse("Fatal error (502)",false)
                }
            }catch (e : Exception){
                response = FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
        }
        return response!!
    }

    suspend fun rateMovie(movieId : String, rating : Double) : FirebaseResponse{
        var response : FirebaseResponse?=null
        withContext(Dispatchers.IO){
            try {
                userRef.collection("watched").document(movieId).update(hashMapOf("rating" to rating) as Map<String, Any>)
                .addOnSuccessListener {
                    response = FirebaseResponse("Movie rated successfully!",true)
                }.addOnFailureListener {
                    response = FirebaseResponse("Something went wrong! Please try rating again",false)
                }.await()
                if (response==null){
                    response = FirebaseResponse("Fatal error (502)",false)
                }
            }catch (e : Exception){
                response = FirebaseResponse("You cannot rate a movie you haven't watched!",false)
            }
        }
        return response!!
    }

    suspend fun getAnyCollection(collection : String,_uid : String?) : ArrayList<FirebaseMovieModel>{
        val result = arrayListOf<FirebaseMovieModel>()
        return withContext(Dispatchers.IO){
            try {
                //not your profile
                uid = _uid ?: originalUid
                val request = firestore.collection("users").document(uid).collection(collection).get().await()
                request.documents.forEach {
                    result.add(it.toObject(FirebaseMovieModel::class.java)!!)
                }
            }catch (e : Exception){
                Log.e("COWBOY FROM FIREBASE REPO->","uid: $uid, ${e.localizedMessage}")
            }
            result
        }
    }
    suspend fun removeFromWatched(movieId : String){
        withContext(Dispatchers.IO){
            try {
                userRef.collection("watched").document(movieId).delete().await()
            }catch (_:Exception){}
        }
    }
    suspend fun removeFromLiked(movieId : String){
        withContext(Dispatchers.IO){
            try {
                userRef.collection("liked").document(movieId).delete().await()
                userRef.collection("watched").document(movieId).update(hashMapOf("liked" to "false") as Map<String, Any>).await()
            }catch (_:Exception){}
        }
    }
    suspend fun removeFromWatchListed(movieId : String){
        withContext(Dispatchers.IO){
            try {
                userRef.collection("watchListed").document(movieId).delete().await()
            }catch (_:Exception){}
        }
    }

    suspend fun addToFriends(friendUid : String,friendDisplayName:String) : FirebaseResponse{
        return withContext(Dispatchers.IO){
            try {
                val dataToSet = FirebaseFriendModel(friendUid,friendDisplayName)
                val request = firestore.collection("users").document(originalUid).collection("friends").document(friendUid).set(dataToSet)
                request.await()
                if (request.isSuccessful){
                    return@withContext FirebaseResponse("You are now following $friendDisplayName",true)
                }else{
                    return@withContext FirebaseResponse("Failure",false)
                }
            }catch (e : Exception){
                Log.e("FOLLOW COWBOY",e.toString())
                return@withContext FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
        }
    }

    suspend fun removeFromFriends(uid : String): FirebaseResponse{
        return withContext(Dispatchers.IO){
            try {
                val request = firestore.collection("users").document(originalUid).collection("friends").document(uid).delete()
                request.await()
                if (request.isSuccessful){
                    return@withContext FirebaseResponse("Unfollowed",true)
                }else{
                    return@withContext FirebaseResponse("Failure",false)
                }
            }catch (e : Exception){
                Log.e("FOLLOW COWBOY",e.toString())
                 return@withContext FirebaseResponse(e.localizedMessage?:"Internal error (501)",false)
            }
        }
    }

    suspend fun getFriendsStatus(uid : String) : FirebaseResponse{
        var result = FirebaseResponse("",false)
        return withContext(Dispatchers.IO){
            try {
                val request = firestore.collection("users").document(originalUid).collection("friends").document(uid).get().await()
                if (request.exists()){
                    result = FirebaseResponse("Following",true)
                }
            }catch (_:Exception){ }
            result
        }
    }

    suspend fun getAllFriends() : ArrayList<FirebaseUserModel>{
        val friendList = arrayListOf<FirebaseUserModel>()
        return withContext(Dispatchers.IO){
            try{
                val request = firestore.collection("users").document(originalUid).collection("friends").get().await()
                request.documents.forEach {
                    val obj = FirebaseUserModel(it["uid"].toString(),it["name"].toString(),"is not shown here")
                    friendList.add(obj)
                }
            }catch (_: Exception){ }
            friendList
        }
    }

    suspend fun getFriendsMovies() : ArrayList<FirebaseFriendMovieModel>{
        val result = arrayListOf<FirebaseFriendMovieModel>()
        return withContext(Dispatchers.IO){
            try {
                //Firstly, try to fetch friends
                val friends = getAllFriends()
                for (i in 0 until friends.size){
                    val currentFriend = friends[i]
                    val initialResponse = getAnyCollection("watched",currentFriend.uid)
                    initialResponse.forEach {
                        result.add(FirebaseFriendMovieModel(it.id,it.name,it.url,it.rating,"by ${currentFriend.displayName}",it.liked))
                    }
                }
            }catch (e:Exception){
                Log.e("COWBOY FRIEND",e.toString())
            }
            result
        }
    }

    suspend fun addReview(id : String,title : String,url:String, content : String,rating: Double) : FirebaseResponse{
        return withContext(Dispatchers.IO){
            try {
                val name = getDisplayName(originalUid)
                val dataToSet = FirebaseReviewModel(id, title, url, content,name, originalUid,rating)
                val request = firestore.collection("users").document(originalUid).collection("reviews").document(id)
                    .set(dataToSet)
                request.await()
                if (request.isSuccessful){
                    return@withContext FirebaseResponse("Added successfully",true)
                }else{
                    return@withContext FirebaseResponse("Something went wrong",false)
                }
            }catch (e:Exception){
                return@withContext FirebaseResponse("Something went wrong",false)
            }
        }
    }

    suspend fun getUserReviews(uid : String? , filmId : String?) : ArrayList<FirebaseReviewModel>{
        val result = arrayListOf<FirebaseReviewModel>()
        return withContext(Dispatchers.IO){
            try {
                val currentUid = uid?:originalUid
                val request = if (filmId == null){
                    firestore.collection("users").document(currentUid).collection("reviews").get().await()
                }else{
                    firestore.collection("users").document(currentUid).collection("reviews").whereEqualTo("id",filmId).get().await()
                }
                request.documents.forEach {
                    result.add(it.toObject(FirebaseReviewModel::class.java)!!)
                }
                result
            }catch (e:Exception){
                result
            }
        }
    }

    suspend fun getFriendReviews(movieId : String) : ArrayList<FirebaseReviewModel>{
        val result = arrayListOf<FirebaseReviewModel>()
        return withContext(Dispatchers.IO){
            try {
                val request = firestore.collection("users").document(originalUid).collection("friends").get().await()
                request.documents.forEach {
                    result.addAll(getUserReviews(it.get("uid").toString(),movieId))
                }
            }catch (_:Exception){ }
            Log.e("SERIOUS COWBOY",result.toString())
            result
        }
    }

    suspend fun addProfilePhoto(uri : Uri) : FirebaseResponse{
        return withContext(Dispatchers.IO){
            try {
                val request = storage.getReference("pfp/$originalUid").putFile(uri)
                request.await()
                return@withContext if (request.isSuccessful){
                    FirebaseResponse("Uploaded successfully!",true)
                }else{
                    FirebaseResponse("Failed to upload, changes won't be saved!",false)
                }
            }catch (e:Exception){
                return@withContext FirebaseResponse(e.localizedMessage?:"Internal error (502)",false)
            }
        }
    }

    //this function will return whether it is watched,liked,added to the list or rated
    suspend fun getMovieActivity(movieId : String) : MovieActivityModel{
        var isWatched = false
        var isLiked = false
        var rating = 0.0
        var isWatchListed = false
        return withContext(Dispatchers.IO){
            try {
                val response = userRef.collection("watched").document(movieId).get().await()
                if (response.exists()){
                    isWatched = true
                    rating = response["rating"].toString().toDouble()
                }
            }catch (e: Exception){
                Log.e("COWBOY BAD FROM MOVIE ACT",e.localizedMessage?:"idk error")
            }
            try {
                isLiked =  userRef.collection("liked").document(movieId).get().await().exists()
            }catch (e:Exception){
                Log.e("COWBOY BAD FROM MOVIE ACT",e.localizedMessage?:"idk error")
            }
            try {
                isWatchListed = userRef.collection("watchListed").document(movieId).get().await().exists()
            }catch (e: Exception){
                Log.e("COWBOY BAD FROM MOVIE ACT",e.localizedMessage?:"idk error")
            }
            Log.e("FAKE COWBOY FROM REPO",MovieActivityModel(isWatched,isLiked, isWatchListed, rating).toString())
            MovieActivityModel(isWatched,isLiked, isWatchListed, rating)
        }
    }
}