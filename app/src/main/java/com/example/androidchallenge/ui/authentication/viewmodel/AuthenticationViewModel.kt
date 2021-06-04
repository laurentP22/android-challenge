package com.example.androidchallenge.ui.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.repository.UserRepository
import com.example.androidchallenge.data.response.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _auth = MutableLiveData<Result<Unit>>()
    val auth: LiveData<Result<Unit>>; get() = _auth

    var pin0: String? = null
    var pin1: String? = null
    var pin2: String? = null
    var pin3: String? = null

    fun save() {
        if (pin0.isNullOrBlank() || pin1.isNullOrBlank() || pin2.isNullOrBlank() || pin3.isNullOrBlank()) {
            return
        }

        val pin = "$pin0$pin1$pin2$pin3".toInt()
        viewModelScope.launch {
            _auth.value = userRepository.authenticate(pin)
        }
    }
}