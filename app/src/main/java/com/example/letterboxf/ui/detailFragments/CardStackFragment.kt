package com.example.letterboxf.ui.detailFragments


import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseCardStackAdapter
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentCardStackBinding
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import st235.com.github.stacklayoutmanager.StackItemTouchHelperCallback
import st235.com.github.stacklayoutmanager.StackLayoutManager

@AndroidEntryPoint
class CardStackFragment : BaseFragment<FragmentCardStackBinding>(FragmentCardStackBinding::inflate) {

    private val adapter = FirebaseCardStackAdapter()
    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()
    private val args : CardStackFragmentArgs by navArgs()

    override fun onViewCreatedLight() {

        firebaseDatabaseViewModel.getWatched(args.uid)
        observe()
        setRv()
        goBack(binding.buttonProfile)
        binding.textViewNothing.visibility = View.VISIBLE
    }

    private fun observe(){
        with(firebaseDatabaseViewModel){
            movies.observe(viewLifecycleOwner){
                if (adapter.currentList().isEmpty()){
                    adapter.updateAdapter(it)
                }
            }
        }
    }


    private fun setRv(){
        val rv = binding.rvStack
        val layoutManager = StackLayoutManager(3)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        adapter.onClick={
            findNavController().navigate(CardStackFragmentDirections.actionCardStackFragmentToMovieDetailFragment(it))
        }
        val callback = object : StackItemTouchHelperCallback() {
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder?,
                position: Int,
                direction: Direction
            ) {
                adapter.remove(position)
            }

            override fun onSwiping(
                viewHolder: RecyclerView.ViewHolder?,
                progress: Float,
                direction: Direction
            ) {
                val vh = (viewHolder as FirebaseCardStackAdapter.FirebaseCardStackViewHolder)
                vh.itemView.rotation = 4 * progress
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun goBack(button: Button){
        button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}