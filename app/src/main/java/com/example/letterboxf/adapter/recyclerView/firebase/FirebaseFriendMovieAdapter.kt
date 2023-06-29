package com.example.letterboxf.adapter.recyclerView.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemFirebaseFriendMovieBinding
import com.example.letterboxf.model.firebase.FirebaseFriendMovieModel

class FirebaseFriendMovieAdapter : RecyclerView.Adapter<FirebaseFriendMovieAdapter.FirebaseFMViewHolder>() {
    inner class FirebaseFMViewHolder(val itemFirebaseFriendMovieBinding: ItemFirebaseFriendMovieBinding) : RecyclerView.ViewHolder(itemFirebaseFriendMovieBinding.root)

    private val dataSet = arrayListOf<FirebaseFriendMovieModel>()
    lateinit var onClick : (id : String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseFMViewHolder {
        val binding = ItemFirebaseFriendMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FirebaseFMViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: FirebaseFMViewHolder, position: Int) {
        val movie = dataSet[position]
        val binding = holder.itemFirebaseFriendMovieBinding
        binding.movie = movie
        binding.main.setOnClickListener {
            onClick.invoke(movie.id)
        }
    }
    fun updateAdapter(newData : ArrayList<FirebaseFriendMovieModel>){
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }
}