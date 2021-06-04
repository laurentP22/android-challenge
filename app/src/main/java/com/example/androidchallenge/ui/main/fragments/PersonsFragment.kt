package com.example.androidchallenge.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.androidchallenge.R
import com.example.androidchallenge.databinding.FragmentPersonsBinding
import com.example.androidchallenge.util.BaseFragment

class PersonsFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPersonsBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_persons, container, false)
        return binding.root
    }
}