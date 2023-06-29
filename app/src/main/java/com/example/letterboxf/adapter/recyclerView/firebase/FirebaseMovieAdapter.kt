package com.example.letterboxf.adapter.recyclerView.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemFirebaseMovieBinding
import com.example.letterboxf.model.firebase.FirebaseMovieModel

class FirebaseMovieAdapter : RecyclerView.Adapter<FirebaseMovieAdapter.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(val itemFirebaseMovieBinding: ItemFirebaseMovieBinding) : RecyclerView.ViewHolder(itemFirebaseMovieBinding.root)

    private val movieList = arrayListOf<FirebaseMovieModel>()
    lateinit var onClick : (id : String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val binding = ItemFirebaseMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FirebaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val movie = movieList[position]
        val binding = holder.itemFirebaseMovieBinding
        binding.movie = movie
        binding.main.setOnClickListener {
            onClick.invoke(movie.id)
        }
    }

    fun updateAdapter(dataSet : ArrayList<FirebaseMovieModel>){
        movieList.clear()
        movieList.addAll(dataSet)
        notifyDataSetChanged()
    }

}