package com.example.letterboxf.adapter.recyclerView.forTextViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemTextBinding
import com.example.letterboxf.model.custom.TextData

class TextViewAdapter : RecyclerView.Adapter<TextViewAdapter.TextViewHolder>() {
    inner class TextViewHolder (val itemTextBinding: ItemTextBinding) : RecyclerView.ViewHolder(itemTextBinding.root)

    private val textList = arrayListOf<TextData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val binding = ItemTextBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TextViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return textList.size
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val text = textList[position]
        val binding = holder.itemTextBinding
        binding.textData = text
    }

    fun updateAdapter(dataSet : List<TextData>){
        textList.clear()
        textList.addAll(dataSet)
        notifyDataSetChanged()
    }
}