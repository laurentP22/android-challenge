package com.example.androidchallenge.ui.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.constant.Constants.Companion.BIOMETRIC
import com.example.androidchallenge.constant.Constants.Companion.NONE
import com.example.androidchallenge.constant.Constants.Companion.PIN
import com.example.androidchallenge.data.repository.UserRepository
import com.example.androidchallenge.data.response.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _auth = MutableLiveData<Result<Unit>>()
    val auth: LiveData<Result<Unit>>; get() = _auth

    private val _enableBiometric = MutableLiveData(false)
    val enableBiometric: LiveData<Boolean>; get() = _enableBiometric

    private val _currentAuthMode = MutableLiveData<String>()

    var pin0: String? = null
    var pin1: String? = null
    var pin2: String? = null
    var pin3: String? = null

    init {
        getAuthMode()
    }

    private fun getAuthMode() {
        viewModelScope.launch {
            _currentAuthMode.value = userRepository.getAuthMode()
            _enableBiometric.value = _currentAuthMode.value == BIOMETRIC
        }
    }

    fun enableBiometric() {
        _enableBiometric.value = !_enableBiometric.value!!
    }

    fun isAuthEnabled(): Boolean = _currentAuthMode.value != NONE

    fun save() {
        if (pin0.isNullOrBlank() || pin1.isNullOrBlank() || pin2.isNullOrBlank() || pin3.isNullOrBlank()) {
            return
        }

        val pin = "$pin0$pin1$pin2$pin3".toInt()
        val mode = if (_enableBiometric.value == true) BIOMETRIC else PIN
        viewModelScope.launch {
            _auth.value = userRepository.setAuthMode(pin, mode)
        }
    }
}