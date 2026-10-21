package com.example.mylabs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserProfileRepository(application)

    val firstName: StateFlow<String> = repository.firstName
    val lastName: StateFlow<String> = repository.lastName
    val address: StateFlow<String> = repository.address
    val saveDataEnabled: StateFlow<Boolean> = repository.saveDataEnabled

    fun onFirstNameChange(value: String) = repository.updateFirstName(value)
    fun onLastNameChange(value: String) = repository.updateLastName(value)
    fun onAddressChange(value: String) = repository.updateAddress(value)
    fun onSaveDataChanged(enabled: Boolean) = repository.setSaveDataEnabled(enabled)
}