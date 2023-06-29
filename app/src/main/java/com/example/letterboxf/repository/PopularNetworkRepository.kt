package com.example.letterboxf.repository

import android.util.Log
import com.example.letterboxf.utils.Constants.API_KEY
import com.example.letterboxf.api.ApiUtils
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PopularNetworkRepository @Inject constructor(apiUtils: ApiUtils) {

    private val popularApi = apiUtils.popularApi
    private val sameForAllApi = apiUtils.sameForAllApi

    private var cachedReviewData: ArrayList<PopularMoviesResult>? = null

    suspend fun getPopularMovies(page: Int): ArrayList<PopularMoviesResult> {
        var results = ArrayList<PopularMoviesResult>()
        return withContext(Dispatchers.IO) {
            try {
                val model = popularApi.getPopularMovies(API_KEY, page)
                if (model.isSuccessful) {
                    model.body()?.let {
                        results = it.results as ArrayList<PopularMoviesResult>
                    }
                }
            } catch (e: Exception) {
                Log.e("COWBOY", e.localizedMessage ?: "Null error message")
            }
            results
        }
    }

    suspend fun getPopularReviews(page: Int): ArrayList<PopularMoviesResult> {
        var movieResults = ArrayList<PopularMoviesResult>()
        val reviewResults = ArrayList<PopularMoviesResult>()
        return withContext(Dispatchers.IO) {
            return@withContext if (cachedReviewData != null){
                cachedReviewData!!
            }else{
                try {
                    val model = popularApi.getPopularMovies(API_KEY, page)
                    if (model.isSuccessful) {
                        model.body()?.let {
                            movieResults = it.results as ArrayList<PopularMoviesResult>
                        }
                        movieResults.forEach { movie ->
                            try{
                                val reviewsResponse = sameForAllApi.getReviews(movie.id, API_KEY)
                                if (reviewsResponse.isSuccessful) {
                                    reviewsResponse.body().let { reviewResponse ->
                                        reviewResponse!!.results.forEach { review ->
                                            val reviewToAdd = PopularMoviesResult(
                                                movie.adult, review.authorDetails.avatarPath,
                                                movie.genreIds, movie.id, movie.originalLanguage,
                                                review.author, review.content, movie.popularity,
                                                movie.posterPath, movie.releaseDate, movie.title,
                                                movie.video, movie.voteAverage, movie.voteCount
                                            )
                                            reviewResults.add(reviewToAdd)
                                        }
                                    }
                                }
                            }
                            catch (e:Exception){
                                Log.e("COWBOY FROM REVIEWS (FETCHING A REVIEW)", e.localizedMessage ?: "Null error message")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("COWBOY FROM REVIEWS (FETCHING A MOVIE)", e.localizedMessage ?: "Null error message")
                }

                cachedReviewData = reviewResults
                cachedReviewData!!.shuffle()
                reviewResults
            }
        }
    }

    suspend fun getPopularPeople (page : Int) : ArrayList<com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult>{
        var result = arrayListOf<com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult>()
        return withContext(Dispatchers.IO){
            try{
                val response = popularApi.getPopularPeople(API_KEY,page)
                if (response.isSuccessful){
                    response.body()?.let {
                        result = it.results as ArrayList<com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult>
                    }
                }
            }
            catch (e:Exception){
                Log.e("COWBOY FROM NETWORK REPOSITORY", e.localizedMessage ?: "Null error message")
            }
            result
        }
    }
}