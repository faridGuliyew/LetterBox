package com.example.letterboxf.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemPopularReviewBinding
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.model.apiModels.movies.Genre
import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.ProductionCompany
import com.example.letterboxf.model.apiModels.movies.ProductionCountry
import com.example.letterboxf.model.apiModels.movies.SpokenLanguage

class PopularReviewsAdapter : RecyclerView.Adapter<PopularReviewsAdapter.PopularReviewsViewHolder>() {
    inner class PopularReviewsViewHolder(val itemPopularReviewBinding: ItemPopularReviewBinding) : RecyclerView.ViewHolder(itemPopularReviewBinding.root)

    private val movieList = arrayListOf<PopularMoviesResult>()
    lateinit var onMovieClick : (String)->Unit
    lateinit var onLongClick : (MovieDetailsResponse)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularReviewsViewHolder {
        val binding = ItemPopularReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularReviewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: PopularReviewsViewHolder, position: Int) {
        val movie = movieList[position]
        val binding = holder.itemPopularReviewBinding
        binding.movie = movie
        with(binding){
            imageViewPopularMovie.setOnClickListener {
                onMovieClick.invoke(movie.id)
            }
            imageViewPopularMovie.setOnLongClickListener {
                onLongClick.invoke(MovieDetailsResponse(movie.adult, movie.backdropPath?:"",
                    Any(),1, listOf(Genre(0,"")),"",movie.id,"",movie.originalLanguage,movie.originalTitle,
                    movie.overview,movie.popularity,movie.posterPath,
                    listOf(ProductionCompany(0,"","","")),
                    listOf(ProductionCountry("","")),movie.releaseDate,1,1,
                    listOf(SpokenLanguage("","","")),"","",movie.title,movie.video,movie.voteAverage,movie.voteCount))
                true
            }
        }
    }
    fun updateAdapter (newDataSet : ArrayList<PopularMoviesResult>){
        movieList.clear()
        movieList.addAll(newDataSet)
        notifyDataSetChanged()
    }
}