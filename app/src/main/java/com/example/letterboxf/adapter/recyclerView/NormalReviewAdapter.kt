package com.example.letterboxf.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemReviewBinding
import com.example.letterboxf.model.apiModels.reviews.ReviewsResult

class NormalReviewAdapter : RecyclerView.Adapter<NormalReviewAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(val itemReviewBinding: ItemReviewBinding) : RecyclerView.ViewHolder(itemReviewBinding.root)

    private val reviewList = arrayListOf<ReviewsResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        val binding = holder.itemReviewBinding
        binding.review = review
    }

    fun updateAdapter(dataSet : List<ReviewsResult>){
        reviewList.clear()
        reviewList.addAll(dataSet)
        notifyDataSetChanged()
    }
}