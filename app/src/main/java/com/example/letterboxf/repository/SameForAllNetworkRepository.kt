package com.example.letterboxf.repository

import android.util.Log
import com.example.letterboxf.utils.Constants.API_KEY
import com.example.letterboxf.api.ApiUtils
import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.credits.MovieCreditsResponse
import com.example.letterboxf.model.apiModels.people.detail.PersonDetailResponse
import com.example.letterboxf.model.apiModels.people.movies.PersonMoviesResponse
import com.example.letterboxf.model.apiModels.reviews.ReviewsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SameForAllNetworkRepository @Inject constructor(apiUtils: ApiUtils) {

    private val sameForAllApi = apiUtils.sameForAllApi

    suspend fun getMovieDetails(movieId : String) : MovieDetailsResponse?{
        var response : MovieDetailsResponse? = null
        return withContext(Dispatchers.IO){
            try {
                val request = sameForAllApi.getMovieDetails(movieId,API_KEY)
                if (request.isSuccessful){
                    request.body().let {
                        response = it!!
                    }
                }
            }
            catch (e:Exception){
                Log.e("COWBOY FROM SFA Network Repo->",e.toString())
                response=null
            }
            response
        }
    }

    suspend fun getMovieCredits(movieId : String) : MovieCreditsResponse?{
        var response : MovieCreditsResponse? = null
        return withContext(Dispatchers.IO){
            try {
                val request = sameForAllApi.getMovieCredits(movieId,API_KEY)
                if (request.isSuccessful){
                    request.body().let {
                        response = it!!
                    }
                }
            }
            catch (e:Exception){
                Log.e("COWBOY FROM SFA Network Repo->",e.toString())
                response=null
            }
            response
        }
    }

    suspend fun getMovieReviews(movieId : String) : ArrayList<ReviewsResult>?{
        var reviewList : ArrayList<ReviewsResult>? = null
        return withContext(Dispatchers.IO){
            try{
                val reviewsResponse = sameForAllApi.getReviews(movieId, API_KEY)
                if (reviewsResponse.isSuccessful) {
                    reviewsResponse.body().let { reviewResponse ->
                        reviewList = reviewResponse!!.results as ArrayList<ReviewsResult>
                    }
                }
            }
            catch (e:Exception){
                Log.e("COWBOY FROM REVIEWS (FETCHING A REVIEW)", e.localizedMessage ?: "Null error message")
                reviewList = null
            }
            reviewList
        }

    }

    suspend fun getActorDetails (actorId : String) : PersonDetailResponse?{
        var response : PersonDetailResponse? = null
        return withContext(Dispatchers.IO){
            try {
                val result = sameForAllApi.getActorDetails(actorId, API_KEY)
                if (result.isSuccessful){
                    result.body().let {
                        response = it
                    }
                }else{
                    Log.e("COWBOY FROM ACTORS1", result.raw().body().toString())
                }
            }catch (e:Exception){
                response = null
                Log.e("COWBOY FROM ACTORS2",e.toString())
            }
            response
        }
    }

    suspend fun getPersonCredits (actorId : String) : PersonMoviesResponse?{
        var response : PersonMoviesResponse? = null
        return withContext(Dispatchers.IO){
            try {
                val result = sameForAllApi.getActorCredits(actorId, API_KEY)
                if (result.isSuccessful){
                    result.body().let {
                        response = it
                    }
                }else{
                    Log.e("COWBOY FROM CREDITS1", result.raw().body().toString())
                }
            }catch (e:Exception){
                response = null
                Log.e("COWBOY FROM CREDITS2",e.toString())
            }
            response
        }
    }
}