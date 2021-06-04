package com.example.androidchallenge.ui.authentication.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.constant.Constants.Companion.AUTH_MODE
import com.example.androidchallenge.data.response.ResultObserver
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.databinding.FragmentAuthenticationBinding
import com.example.androidchallenge.ui.authentication.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : DialogFragment() {
    private val viewModel: AuthenticationViewModel by viewModels()
    private lateinit var binding: FragmentAuthenticationBinding
    private val previousSavedStateHandle: SavedStateHandle? by lazy {
        findNavController().previousBackStackEntry?.savedStateHandle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previousSavedStateHandle?.set(AUTH_MODE, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.auth.observe(viewLifecycleOwner, ResultObserver {
            var text = getString(R.string.wrong_pin)
            if (it.status == Status.SUCCESS) {
                text = getString(R.string.good_pin)
                previousSavedStateHandle?.set(AUTH_MODE, true)
                findNavController().popBackStack()
            }
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        })

        // TODO Improve the following listeners
        binding.etPin0.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.etPin1.requestFocus()
            }
        }

        binding.etPin1.apply {
            addTextChangedListener { if (!it.isNullOrBlank()) binding.etPin2.requestFocus() }
            setOnKeyListener { _, _, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_DEL && text.isNullOrBlank()) {
                    binding.etPin0.requestFocus()
                    binding.etPin0.text.clear()
                }
                false
            }
        }

        binding.etPin2.apply {
            addTextChangedListener { if (!it.isNullOrBlank()) binding.etPin3.requestFocus() }
            setOnKeyListener { _, _, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_DEL && text.isNullOrBlank()) {
                    binding.etPin1.requestFocus()
                    binding.etPin1.text.clear()
                }
                false
            }
        }

        binding.etPin3.apply {
            addTextChangedListener {
                if (!it.isNullOrBlank()) {
                    dialog?.currentFocus?.let { v ->
                        val imm =
                            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    binding.etPin3.clearFocus()
                }
            }
            setOnKeyListener { _, _, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_DEL && text.isNullOrBlank()) {
                    binding.etPin2.requestFocus()
                    binding.etPin2.text.clear()
                }
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Adapt the width of the dialog (90% of the screen's width)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}