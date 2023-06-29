package com.example.letterboxf.adapter.recyclerView.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.databinding.ItemFirebaseMovieBinding
import com.example.letterboxf.databinding.ItemFirebaseUserBinding
import com.example.letterboxf.model.firebase.FirebaseUserModel

class FirebaseUserAdapter : RecyclerView.Adapter<FirebaseUserAdapter.UserViewHolder>() {
    inner class UserViewHolder(val itemFirebaseUserBinding: ItemFirebaseUserBinding) : RecyclerView.ViewHolder(itemFirebaseUserBinding.root)

    private val userList = arrayListOf<FirebaseUserModel>()
    lateinit var onClick : (id : String) ->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemFirebaseUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val binding = holder.itemFirebaseUserBinding
        binding.user = user
        //Click events
        binding.root.setOnClickListener {
            onClick.invoke(user.uid)
        }
    }

    fun updateAdapter(dataSet : ArrayList<FirebaseUserModel>){
        userList.clear()
        userList.addAll(dataSet)
        notifyDataSetChanged()
    }
}