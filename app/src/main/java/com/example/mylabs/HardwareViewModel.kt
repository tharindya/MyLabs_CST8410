package com.example.mylabs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HardwareViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HardwareRepository()

    val wifiState: StateFlow<String> = repository.wifiEnabled
        .map { if (it) "On" else "Off" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Off")

    val btState: StateFlow<String> = repository.bluetoothEnabled
        .map { if (it) "On" else "Off" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Off")

    val flashlightState: StateFlow<String> = repository.flashlightOn
        .map { if (it) "On" else "Off" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Off")

    val lightValue: StateFlow<String> = repository.lightValue
    val accelerometer: StateFlow<String> = repository.accelerometer
    val stepCount: StateFlow<String> = repository.stepCount

    fun setWifiEnabled(enabled: Boolean) = repository.setWifiEnabled(enabled)
    fun setBluetoothEnabled(enabled: Boolean) = repository.setBluetoothEnabled(enabled)
    fun setFlashlightOn(on: Boolean) = repository.setFlashlightOn(on)
    fun setLightValue(value: Float) = repository.setLightValue(value)
    fun setAccelerometer(x: Float, y: Float, z: Float) = repository.setAccelerometer(x, y, z)
    fun setStepCount(steps: Float?) = repository.setStepCount(steps)
}