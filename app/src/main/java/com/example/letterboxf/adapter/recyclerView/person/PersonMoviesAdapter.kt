package com.example.letterboxf.adapter.recyclerView.person

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemNormalMovieBinding
import com.example.letterboxf.model.apiModels.people.movies.Cast

class PersonMoviesAdapter : RecyclerView.Adapter<PersonMoviesAdapter.MovieViewHolder>() {
    inner class MovieViewHolder (val itemNormalMovieBinding: ItemNormalMovieBinding) : RecyclerView.ViewHolder(itemNormalMovieBinding.root)

    private val movieSet = arrayListOf<Cast>()
    lateinit var onClick : (movieId : String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemNormalMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieSet.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieSet[position]
        val binding = holder.itemNormalMovieBinding
        binding.movie=movie
        with(binding){
            main.setOnClickListener {
                onClick.invoke(movie.id)
            }
        }
    }
    fun updateAdapter(dataSet : List<Cast>){
        movieSet.clear()
        movieSet.addAll(dataSet)
        notifyDataSetChanged()
    }
}