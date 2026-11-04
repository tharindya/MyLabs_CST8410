package com.example.mylabs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HardwareRepository {

    private val _wifiEnabled = MutableStateFlow(false)
    val wifiEnabled: StateFlow<Boolean> = _wifiEnabled.asStateFlow()

    private val _bluetoothEnabled = MutableStateFlow(false)
    val bluetoothEnabled: StateFlow<Boolean> = _bluetoothEnabled.asStateFlow()

    private val _flashlightOn = MutableStateFlow(false)
    val flashlightOn: StateFlow<Boolean> = _flashlightOn.asStateFlow()

    private val _lightValue = MutableStateFlow("0.0000")
    val lightValue: StateFlow<String> = _lightValue.asStateFlow()

    private val _accelerometer = MutableStateFlow("0.000, 0.000, 0.000")
    val accelerometer: StateFlow<String> = _accelerometer.asStateFlow()

    private val _stepCount = MutableStateFlow("Not available")
    val stepCount: StateFlow<String> = _stepCount.asStateFlow()

    fun setWifiEnabled(enabled: Boolean) {
        _wifiEnabled.value = enabled
    }

    fun setBluetoothEnabled(enabled: Boolean) {
        _bluetoothEnabled.value = enabled
    }

    fun setFlashlightOn(on: Boolean) {
        _flashlightOn.value = on
    }

    fun setLightValue(value: Float) {
        _lightValue.value = "%.4f".format(value)
    }

    fun setAccelerometer(x: Float, y: Float, z: Float) {
        _accelerometer.value = "%.3f, %.3f, %.3f".format(x, y, z)
    }

    fun setStepCount(steps: Float?) {
        _stepCount.value = steps?.toInt()?.toString() ?: "Not available"
    }
}