package com.example.letterboxf.ui.bottomsheetFragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.letterboxf.R
import com.example.letterboxf.databinding.FragmentAddReviewBinding
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.viewmodel.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReviewFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentAddReviewBinding? = null
    private val binding
        get() = _binding!!

    private val reviewViewModel : ReviewViewModel by viewModels()
    private val args : AddReviewFragmentArgs by navArgs()
    private lateinit var movie : FirebaseMovieModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie = args.movie
        binding.movie = movie
        observe()
        publish(binding.buttonPublish,binding.editTextReview)
    }

    private fun observe(){
        with(reviewViewModel){
            response.observe(viewLifecycleOwner){
                if (it!!.status){
                    FancyToast.makeText(requireContext(), it.text, FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS, R.drawable.ic_logo, false).show()
                    dismiss()
                }else{
                    FancyToast.makeText(requireContext(), it.text,
                        FancyToast.LENGTH_SHORT,FancyToast.ERROR, R.drawable.ic_logo, false).show()
                }
            }
        }
    }

    private fun publish(button: Button, editText : EditText){
        button.setOnClickListener {
            val content = editText.text.toString()
            reviewViewModel.addReview(movie.id,movie.name,movie.url,content,movie.rating)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReviewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}