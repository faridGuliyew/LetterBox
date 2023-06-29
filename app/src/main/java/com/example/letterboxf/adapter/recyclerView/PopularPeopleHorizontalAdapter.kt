package com.example.letterboxf.adapter.recyclerView

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemPopularPersonHorizontalBinding
import com.example.letterboxf.model.apiModels.people.popularPeople.PopularPeopleResult

class PopularPeopleHorizontalAdapter : RecyclerView.Adapter<PopularPeopleHorizontalAdapter.PeopleViewHolder>(){
    inner class PeopleViewHolder (val itemPopularPersonHorizontalBinding: ItemPopularPersonHorizontalBinding) : RecyclerView.ViewHolder(itemPopularPersonHorizontalBinding.root)

    private val peopleList = arrayListOf<PopularPeopleResult>()
    lateinit var onClick : (actorId : String) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding = ItemPopularPersonHorizontalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PeopleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return peopleList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val person = peopleList[position]
        val binding = holder.itemPopularPersonHorizontalBinding
        binding.person = person
        //Bind views
        with(binding){
            var popularFilms = ""
            person.knownFor.forEach { if (it.title != null){ popularFilms += "<b>"+it.title+"</b>" + ", " } }
            textViewPopularPersonDescription.text = Html.fromHtml("Known for popular movies including ${popularFilms}and many more...",HtmlCompat.FROM_HTML_MODE_COMPACT)
            main.setOnClickListener {
                onClick.invoke(person.id)
            }
        }
    }

    fun updateAdapter(newDataSet : ArrayList<PopularPeopleResult>){
        peopleList.clear()
        peopleList.addAll(newDataSet)
        notifyDataSetChanged()
    }
}