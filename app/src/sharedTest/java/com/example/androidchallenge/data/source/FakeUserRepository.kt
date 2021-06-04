package com.example.androidchallenge.data.source

import com.example.androidchallenge.data.repository.UserRepository
import com.example.androidchallenge.data.response.Result
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {
    override suspend fun getAuthMode(): String {
        TODO("Not yet implemented")
    }

    override suspend fun setAuthMode(pin: Int, mode: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(pin: Int): Result<Unit> {
        TODO("Not yet implemented")
    }
}