package com.example.letterboxf.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.utils.Constants
import com.example.letterboxf.databinding.ItemCastOrCrewBinding
import com.example.letterboxf.model.apiModels.movies.credits.Crew
import com.squareup.picasso.Picasso

class CrewAdapter : RecyclerView.Adapter<CrewAdapter.MyViewHolder>(){
    inner class MyViewHolder(val itemCastOrCrewBinding: ItemCastOrCrewBinding) : RecyclerView.ViewHolder(itemCastOrCrewBinding.root)

    private val crewList = arrayListOf<Crew>()
    lateinit var onClick : (actorId: String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCastOrCrewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return crewList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val crewMember = crewList[position]
        val binding = holder.itemCastOrCrewBinding

        var capitalLetters  = ""
        val name = crewMember.originalName
        if (name.isNotEmpty()){
            name.split(" ").forEach {
                capitalLetters += it[0]
            }
        }

        with(binding){
            //dataBinding
            textViewLoaderMemberName.text = capitalLetters
            textViewMemberName.text = crewMember.originalName
            textViewCharacterName.text = crewMember.department
            Picasso.get().load(Constants.IMAGE_BASE +crewMember.profilePath).into(imageViewCastMember)

            main.setOnClickListener {
                onClick.invoke(crewMember.id)
            }
        }
    }
    fun updateAdapter(dataSet : List<Crew>){
        crewList.clear()
        crewList.addAll(dataSet)
        notifyDataSetChanged()
    }
}