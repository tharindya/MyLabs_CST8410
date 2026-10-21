package com.example.mylabs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileRepository(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_profile_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _firstName = MutableStateFlow(sharedPreferences.getString("first_name", "") ?: "")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow(sharedPreferences.getString("last_name", "") ?: "")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _address = MutableStateFlow(sharedPreferences.getString("address", "") ?: "")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _saveDataEnabled = MutableStateFlow(
        sharedPreferences.getBoolean("save_data_enabled", false)
    )
    val saveDataEnabled: StateFlow<Boolean> = _saveDataEnabled.asStateFlow()

    fun updateFirstName(value: String) {
        _firstName.value = value
        if (_saveDataEnabled.value) {
            sharedPreferences.edit().putString("first_name", value).apply()
        }
    }

    fun updateLastName(value: String) {
        _lastName.value = value
        if (_saveDataEnabled.value) {
            sharedPreferences.edit().putString("last_name", value).apply()
        }
    }

    fun updateAddress(value: String) {
        _address.value = value
        if (_saveDataEnabled.value) {
            sharedPreferences.edit().putString("address", value).apply()
        }
    }

    fun setSaveDataEnabled(enabled: Boolean) {
        _saveDataEnabled.value = enabled
        sharedPreferences.edit().putBoolean("save_data_enabled", enabled).apply()

        if (enabled) {
            // Save current values
            sharedPreferences.edit()
                .putString("first_name", _firstName.value)
                .putString("last_name", _lastName.value)
                .putString("address", _address.value)
                .apply()
        } else {
            // Clear everything when user declines
            clearAllData()
        }
    }

    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
        _firstName.value = ""
        _lastName.value = ""
        _address.value = ""
        _saveDataEnabled.value = false
    }
}