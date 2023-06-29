package com.example.letterboxf.repository


import android.util.Log
import com.example.letterboxf.utils.Constants.API_KEY
import com.example.letterboxf.api.ApiUtils
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.model.apiModels.search.SearchPersonResult
import com.example.letterboxf.model.firebase.FirebaseUserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchNetworkRepository @Inject constructor(apiUtils : ApiUtils, private val firestore : FirebaseFirestore) {

    private val searchApi = apiUtils.searchApi

    suspend fun getMovies(query : String) : ArrayList<PopularMoviesResult>{
        var searchResult = arrayListOf<PopularMoviesResult>()
        return withContext(Dispatchers.IO){
            try {
                val result = searchApi.searchMovie(API_KEY,query)
                if (result.isSuccessful){
                    result.body().let {
                        searchResult = it!!.results as ArrayList<PopularMoviesResult>
                    }
                }
            }catch (e:Exception){
                Log.e("COWBOY FROM SEARCH",e.toString())
            }
            searchResult
        }
    }

    suspend fun getPeople(query : String) : ArrayList<SearchPersonResult>{
        var searchResult = arrayListOf<SearchPersonResult>()
        return withContext(Dispatchers.IO){
            try {
                val result = searchApi.searchPerson(API_KEY,query)
                if (result.isSuccessful){
                    result.body().let {
                        searchResult = it!!.searchPersonResults as ArrayList<SearchPersonResult>
                    }
                }
            }catch (e:Exception){
                Log.e("COWBOY FROM SEARCH",e.toString())
            }
            searchResult
        }
    }

    suspend fun getUsers(query: String) : ArrayList<FirebaseUserModel>{
        val result = arrayListOf<FirebaseUserModel>()
        return withContext(Dispatchers.IO){
            try {
                //todo preventing user from viewing all the account is preferred
                firestore.collection("users")
                    .whereGreaterThanOrEqualTo("displayName",query)
                    .whereLessThanOrEqualTo("displayName", query + "\uf8ff")
                    .get().addOnSuccessListener {
                    it.documents.forEach {doc->
                        result.add(doc.toObject(FirebaseUserModel::class.java)!!)
                        Log.e("INSPECTOR COWBOY",it.toString())
                    }
                }.await()
            }catch (e: Exception){
                Log.e("SEARCH COWBOY",e.localizedMessage?:"unknown error")
            }
            result
        }
    }
}