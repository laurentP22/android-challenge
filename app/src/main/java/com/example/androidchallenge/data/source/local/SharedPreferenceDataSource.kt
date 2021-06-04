package com.example.androidchallenge.data.source.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.androidchallenge.constant.Constants.Companion.AUTH_MODE
import com.example.androidchallenge.constant.Constants.Companion.NONE
import com.example.androidchallenge.constant.Constants.Companion.PIN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferenceDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val mainSharedPref = EncryptedSharedPreferences.create(
        context,
        "com.example.androidchallenge",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    suspend fun getAuthMode(): String =
        withContext(ioDispatcher) { mainSharedPref.getString(AUTH_MODE, NONE) ?: NONE }

    suspend fun setAuthMode(mode: String) =
        withContext(ioDispatcher) { mainSharedPref.edit { putString(AUTH_MODE, mode) } }

    suspend fun getPin(): Int =
        withContext(ioDispatcher) { mainSharedPref.getInt(PIN, 0) }
    suspend fun setPin(pin: Int) =
        withContext(ioDispatcher) { mainSharedPref.edit { putInt(PIN, pin) } }

    suspend fun clear() = withContext(ioDispatcher) {
        mainSharedPref.edit { clear() }
    }
}