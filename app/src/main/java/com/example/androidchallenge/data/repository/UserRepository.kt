package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.response.Result

interface UserRepository {

    /**
     * Get user authentication mode (none, biometric, pin)
     */
    suspend fun getAuthMode(): String

    /**
     * Set user authentication mode (none, biometric, pin).
     * @param pin: Secret code
     * @param mode: Authentication mode (biometric or PIN)
     */
    suspend fun setAuthMode(pin: Int, mode: String) : Result<Unit>

    /**
     * Authenticate the user.
     */
    suspend fun authenticate(pin: Int) : Result<Unit>

}