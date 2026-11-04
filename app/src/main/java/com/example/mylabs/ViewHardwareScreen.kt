package com.example.mylabs

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ViewHardware(
    wifiState: String,
    btState: String,
    fLight: String,
    light: String,
    accel: String,
    step: String,
    onWifiClick: () -> Unit = {},
    onBtClick: () -> Unit = {},
    onFlashlightToggle: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Hardware Status",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HardwareRow(
            label = "Wifi state",
            value = wifiState,
            isSwitch = true,
            checked = wifiState == "On",
            onCheckedChange = { onWifiClick() }
        )

        HardwareRow(
            label = "Bluetooth state",
            value = btState,
            isSwitch = true,
            checked = btState == "On",
            onCheckedChange = { onBtClick() }
        )

        HardwareRow(
            label = "Flashlight",
            value = fLight,
            isSwitch = true,
            checked = fLight == "On",
            onCheckedChange = onFlashlightToggle
        )

        HardwareRow(label = "Lighting Value", value = light)
        HardwareRow(label = "Accelerometer", value = accel)
        HardwareRow(label = "Step Counter", value = step)
    }
}

@Composable
private fun HardwareRow(
    label: String,
    value: String,
    isSwitch: Boolean = false,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        if (isSwitch) {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        } else {
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewHardware() {
    ViewHardware(
        wifiState = "On",
        btState = "Off",
        fLight = "On",
        light = "333.444",
        accel = "0.53, 0.363, 9.63",
        step = "3636"
    )
}

// Helper functions
fun openWifiSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.startActivity(Intent(Settings.Panel.ACTION_WIFI))
    } else {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}

fun openBluetoothSettings(context: Context) {
    context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
}

fun setTorchMode(context: Context, enabled: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        } ?: return
        cameraManager.setTorchMode(cameraId, enabled)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}