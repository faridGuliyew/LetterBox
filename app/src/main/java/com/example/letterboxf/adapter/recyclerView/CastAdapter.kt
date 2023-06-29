package com.example.letterboxf.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.utils.Constants.IMAGE_BASE
import com.example.letterboxf.databinding.ItemCastOrCrewBinding
import com.example.letterboxf.model.apiModels.movies.credits.Cast
import com.squareup.picasso.Picasso

class CastAdapter : RecyclerView.Adapter<CastAdapter.MyViewHolder>() {
    inner class MyViewHolder (val itemCastOrCrewBinding: ItemCastOrCrewBinding) : RecyclerView.ViewHolder(itemCastOrCrewBinding.root)

    private val castList = arrayListOf<Cast>()
    lateinit var onClick : (actorId: String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCastOrCrewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val castMember = castList[position]
        val binding = holder.itemCastOrCrewBinding

        var capitalLetters  = ""
        val name = castMember.originalName
        if (name.isNotEmpty()){
            name.split(" ").forEach {
                capitalLetters += it[0]
            }
        }

        with(binding){
            //dataBinding
            textViewLoaderMemberName.text = capitalLetters
            textViewMemberName.text = castMember.originalName
            textViewCharacterName.text = castMember.character
            Picasso.get().load(IMAGE_BASE+castMember.profilePath).into(imageViewCastMember)

            main.setOnClickListener {
                onClick.invoke(castMember.id)
            }
        }
    }
    fun updateAdapter(dataSet : List<Cast>){
        castList.clear()
        castList.addAll(dataSet)
        notifyDataSetChanged()
    }
}