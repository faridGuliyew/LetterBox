package com.example.letterboxf.adapter.recyclerView.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemNormalPersonBinding
import com.example.letterboxf.model.apiModels.search.SearchPersonResult

class SearchPeopleAdapter  :RecyclerView.Adapter<SearchPeopleAdapter.SearchViewHolder>() {
    inner class SearchViewHolder (val itemNormalPersonBinding: ItemNormalPersonBinding) : RecyclerView.ViewHolder(itemNormalPersonBinding.root)

    private val peopleList = arrayListOf<SearchPersonResult>()
    lateinit var onClick : (actorId : String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemNormalPersonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val person = peopleList[position]
        val binding = holder.itemNormalPersonBinding
        binding.person = person
        //Click event
        with(binding){ main.setOnClickListener { onClick.invoke(person.id) } }
    }
    fun updateAdapter(dataSet : ArrayList<SearchPersonResult>){
        peopleList.clear()
        peopleList.addAll(dataSet)
        notifyDataSetChanged()
    }
}