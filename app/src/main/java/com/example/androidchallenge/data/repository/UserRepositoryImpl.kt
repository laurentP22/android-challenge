package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.source.local.SharedPreferenceDataSource

class UserRepositoryImpl(
    private val sharedPreferenceDataSource: SharedPreferenceDataSource
) : UserRepository {

    override suspend fun getAuthMode(): String = sharedPreferenceDataSource.getAuthMode()

    override suspend fun setAuthMode(pin: Int, mode: String): Result<Unit> {
        sharedPreferenceDataSource.setAuthMode(mode)
        sharedPreferenceDataSource.setPin(pin)
        return Result.success(Unit)
    }

    override suspend fun authenticate(pin: Int): Result<Unit> {
        return if(pin == sharedPreferenceDataSource.getPin()){
            Result.success(Unit)
        }else{
            Result.error(null)
        }
    }
}