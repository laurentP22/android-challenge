package com.example.androidchallenge.util

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.constant.Constants
import com.example.androidchallenge.constant.Constants.Companion.BUNDLE_ARGS

open class BaseFragment : Fragment() {
    protected val navController by lazy {
        try {
            Navigation.findNavController(requireActivity(), R.id.main_nav_host)
        } catch (e: Exception) {
            findNavController()
        }
    }

    override fun setArguments(args: Bundle?) {
        // Configures navArgs to be accessible from the viewModel
        val arguments = args?.let { Bundle(args).apply { putBundle(BUNDLE_ARGS, args) } }
        super.setArguments(arguments)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDestination = navController.currentDestination?.id
        if (currentDestination != null) {
            val navBackStackEntry = navController.getBackStackEntry(currentDestination)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            // Create our observer and add it to the NavBackStackEntry's lifecycle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(Constants.AUTH_MODE)) {
                    val result = savedStateHandle.get<Boolean>(Constants.AUTH_MODE) ?: false
                    if (!result) {
                        requireActivity().finish()
                    }
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)

            // As addObserver() does not automatically remove the observer, we
            // call removeObserver() manually when the view lifecycle is destroyed
            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }
}