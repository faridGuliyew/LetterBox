package com.example.letterboxf.adapter.recyclerView.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemFirebaseReviewBinding
import com.example.letterboxf.model.firebase.FirebaseReviewModel

class FirebaseReviewAdapter : RecyclerView.Adapter<FirebaseReviewAdapter.FRAViewHolder>() {
    inner class FRAViewHolder(val itemFirebaseReviewBinding: ItemFirebaseReviewBinding) : RecyclerView.ViewHolder(itemFirebaseReviewBinding.root)

    private val reviewList = arrayListOf<FirebaseReviewModel>()
    lateinit var onClick : (id : String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FRAViewHolder {
        val binding = ItemFirebaseReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FRAViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: FRAViewHolder, position: Int) {
        val review = reviewList[position]
        val binding = holder.itemFirebaseReviewBinding
        binding.review = review
        binding.imageViewPopularMovie.setOnClickListener {
            onClick.invoke(review.id)
        }
    }

    fun updateAdapter (newData : ArrayList<FirebaseReviewModel>){
        reviewList.clear()
        reviewList.addAll(newData)
        notifyDataSetChanged()
    }
}