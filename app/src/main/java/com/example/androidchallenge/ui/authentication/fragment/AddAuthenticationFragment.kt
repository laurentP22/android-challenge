package com.example.androidchallenge.ui.authentication.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidchallenge.data.response.ResultObserver
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.databinding.FragmentAddAuthenticationBinding
import com.example.androidchallenge.ui.authentication.viewmodel.AddAuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAuthenticationFragment : DialogFragment() {
    private val viewModelAdd: AddAuthenticationViewModel by viewModels()
    private lateinit var binding: FragmentAddAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAuthenticationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModelAdd
        binding.lifecycleOwner = this

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAdd.auth.observe(viewLifecycleOwner, ResultObserver {
            if (it.status == Status.SUCCESS) {
                findNavController().popBackStack()
            }
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