package com.example.letterboxf.ui.mainFragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxf.R
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentProfileBinding
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val likedAdapter = FirebaseMovieAdapter()
    private val watchedAdapter = FirebaseMovieAdapter()

    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()

    private val args : ProfileFragmentArgs by navArgs()
    private var uid : String? = null
    private var userName = "unknown"

    private lateinit var followButton : Button
    private lateinit var profilePhoto : ImageView
    private lateinit var pickMedia  : ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri : Uri? = null

    override fun onViewCreatedLight() {
        initVars()
        bindViews(binding.textViewGoToWatched)
        setFollowButtonInitialLook(binding.buttonFollow,binding.imageButtonEdit)
        controlFollowButton()
        pickImage(binding.imageButtonEdit)
        observeViewModel()
        loadData()
        setRv()
        goToStack(binding.buttonStackMode)
    }

    private fun initVars(){
        uid = args.uid
        followButton = binding.buttonFollow
        profilePhoto = binding.imageViewProfilePicture
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.progressBarPfp.visibility = View.VISIBLE
                imageUri = uri
                firebaseDatabaseViewModel.addProfilePhoto(imageUri!!)
            }
        }
    }

    private fun loadData(){
        with(firebaseDatabaseViewModel){
            uid?.let {
                getFriendStatus(uid!!)
            }
            getProfilePhoto(uid)
            getDisplayName(uid)
            getLiked(uid)
            getWatched(uid)
        }
    }

    private fun observeViewModel(){
        with(firebaseDatabaseViewModel){
            likedMovies.observe(viewLifecycleOwner){
                binding.textViewLikedF.text = it.size.toString()
                likedAdapter.updateAdapter(it)
            }
            movies.observe(viewLifecycleOwner){
                binding.textViewTotalMovie.text = it.size.toString()
                watchedAdapter.updateAdapter(it)
            }
            displayName.observe(viewLifecycleOwner){
                userName = it
                binding.textViewDisplayName.text = it
            }
            uri.observe(viewLifecycleOwner){

                it?.let {
                    binding.progressBarPfp.visibility = View.VISIBLE
                    Picasso.get().load(it).into(profilePhoto,object : Callback{
                        override fun onSuccess() {
                            binding.progressBarPfp.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            binding.progressBarPfp.visibility = View.GONE
                        }
                    })
                }
            }
            addToFriendsResponse.observe(viewLifecycleOwner){
                if (it!!.status){
                    //Added successfully
                    buttonToFollowingMode()
                    FancyToast.makeText(requireContext(),it.text,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,R.drawable.ic_logo,false).show()
                }else{
                    //Failed to add (Toast)
                    buttonToFollowMode()
                }
            }
            removeFromFriendsResponse.observe(viewLifecycleOwner){
                //Log.e("FRIEND COWBOY R",it!!.status.toString())
                if (it!!.status){
                    //Removed successfully
                    buttonToFollowMode()
                    FancyToast.makeText(requireContext(),it.text,FancyToast.LENGTH_SHORT,FancyToast.DEFAULT,R.drawable.ic_logo,false).show()
                }else{
                    //Failed to remove (Toast)
                    buttonToFollowingMode()
                }
            }
            response.observe(viewLifecycleOwner){
                binding.progressBarPfp.visibility = View.GONE
                if (it!!.status){
                    imageUri?.let {_uri->
                        profilePhoto.setImageURI(_uri)
                    }
                }
            }
        }
    }

    private fun setRv(){
        val likedRv = binding.rvLiked
        likedRv.adapter = likedAdapter
        likedAdapter.onClick = {
            setSp(it)
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMovieDetailFragment(it))
        }
        val watchedRv = binding.rvWatched
        watchedRv.adapter = watchedAdapter
        watchedAdapter.onClick = {
            setSp(it)
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMovieDetailFragment(it))
        }

    }

    private fun bindViews(goToWatched : TextView){
        //Click events
        goToWatched.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWatchedMoviesFragment(args.uid))
        }
    }

    //Whether display follow option or not!
    private fun setFollowButtonInitialLook(followButton : Button,editButton : Button){
        if (args.uid == null || args.uid == getUid()){
            followButton.visibility = View.GONE
        }else{
            editButton.visibility = View.GONE
            followButton.visibility = View.VISIBLE
            firebaseDatabaseViewModel.response.observe(viewLifecycleOwner){
                if (it!!.status){
                    buttonToFollowingMode()
                }else{
                    buttonToFollowMode()
                }
            }
        }
    }

    private fun buttonToFollowingMode(){
        followButton.text = "Following"
        followButton.tag = "following"
        followButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.following))
    }
    private fun buttonToFollowMode(){
        followButton.text = "Follow"
        followButton.tag = "follow"
        followButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.follow))
    }

    private fun controlFollowButton(){
        followButton.setOnClickListener{
            when(it.tag){
                "follow"->{firebaseDatabaseViewModel.addToFriends(uid!!,userName)
                }
                "following"->{firebaseDatabaseViewModel.removeFromFriends(uid!!)}
            }
        }
    }

    private fun goToStack(button: Button){
        button.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCardStackFragment(args.uid))
        }
    }

    private fun pickImage(button : Button){
        button.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

    private fun getUid() : String{
        return requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString("uid","")!!
    }
}