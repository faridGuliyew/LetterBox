package com.example.letterboxf.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment <VB : ViewBinding> (private val layoutInflater : (inflater : LayoutInflater) -> VB) : Fragment() {

    private var _binding : VB? = null
    protected val binding
        get() = _binding!!

    protected abstract fun onViewCreatedLight()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = layoutInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedLight()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}