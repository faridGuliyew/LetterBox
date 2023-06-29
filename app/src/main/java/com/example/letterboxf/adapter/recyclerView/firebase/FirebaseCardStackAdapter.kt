package com.example.letterboxf.adapter.recyclerView.firebase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemCardStackBinding
import com.example.letterboxf.model.firebase.FirebaseMovieModel

class FirebaseCardStackAdapter : RecyclerView.Adapter<FirebaseCardStackAdapter.FirebaseCardStackViewHolder>() {
    inner class FirebaseCardStackViewHolder(val itemCardStackBinding: ItemCardStackBinding) : RecyclerView.ViewHolder(itemCardStackBinding.root)

    private val movieList = arrayListOf<FirebaseMovieModel>()
    lateinit var onClick : (id : String)->Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseCardStackViewHolder {
        val binding = ItemCardStackBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FirebaseCardStackViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: FirebaseCardStackViewHolder, position: Int) {
        val movie = movieList[position]
        val binding = holder.itemCardStackBinding
        binding.movie = movie
        Log.e("SERIOUS",movie.liked.toString())
        with(binding){
            likeIcon.visibility = if (movie.liked) View.VISIBLE else View.GONE
            imageViewPosterSmall.setOnClickListener {
                onClick.invoke(movie.id)
            }
        }
    }

    fun updateAdapter(dataSet : ArrayList<FirebaseMovieModel>){
        movieList.clear()
        movieList.addAll(dataSet)
        notifyDataSetChanged()
    }
    fun remove(position : Int){
        movieList.removeAt(position)
        notifyItemRemoved(position)
    }
    fun currentList(): ArrayList<FirebaseMovieModel> {
        return movieList
    }
}