package com.example.letterboxf.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemPopularMovieBinding
import com.example.letterboxf.model.apiModels.PopularMoviesResult
import com.example.letterboxf.model.apiModels.movies.Genre
import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.ProductionCompany
import com.example.letterboxf.model.apiModels.movies.ProductionCountry
import com.example.letterboxf.model.apiModels.movies.SpokenLanguage

class PopularMoviesAdapter : RecyclerView.Adapter<PopularMoviesAdapter.PopularViewHolder>() {
    inner class PopularViewHolder (val itemPopularMovieBinding: ItemPopularMovieBinding) : RecyclerView.ViewHolder(itemPopularMovieBinding.root)

    private val movieList = arrayListOf<PopularMoviesResult>()
    lateinit var onClick : (String)->Unit
    lateinit var onLongClick : (MovieDetailsResponse)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemPopularMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val movie = movieList[position]
        val binding = holder.itemPopularMovieBinding
        binding.movie = movie

        with(binding){
            //Click handlers
            main.setOnClickListener {
                onClick.invoke(movie.id)
            }
            main.setOnLongClickListener {
                onLongClick.invoke(MovieDetailsResponse(false,"",
                    Any(),1, listOf(Genre(0,"")),"",movie.id,"","",movie.originalTitle,
                "",0.0,movie.posterPath,
                    listOf(ProductionCompany(0,"","","")),
                    listOf(ProductionCountry("","")),"",1,1,
                    listOf(SpokenLanguage("","","")),"","",movie.title,false,0.0,0))
                true
            }
        }
    }

    fun updateAdapter(newDataSet : List<PopularMoviesResult>){
        movieList.clear()
        movieList.addAll(newDataSet)
        notifyDataSetChanged()
    }
}