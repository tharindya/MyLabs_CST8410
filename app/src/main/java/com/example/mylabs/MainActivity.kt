package com.example.mylabs

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylabs.ui.theme.MyLabsTheme

class MainActivity : ComponentActivity() {

    private lateinit var hardwareViewModel: HardwareViewModel
    private lateinit var sensorManager: SensorManager

    private var lightSensor: Sensor? = null
    private var accelSensor: Sensor? = null
    private var stepSensor: Sensor? = null

    // ---------- Sensor listeners (only live in MainActivity) ----------
    private val lightListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            hardwareViewModel.setLightValue(event.values[0])
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private val accelListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            hardwareViewModel.setAccelerometer(
                event.values[0], event.values[1], event.values[2]
            )
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            hardwareViewModel.setStepCount(event.values[0])
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    // ---------- Wi-Fi receiver ----------
    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                hardwareViewModel.setWifiEnabled(wifiManager.isWifiEnabled)
            }
        }
    }

    // ---------- Bluetooth receiver ----------
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                hardwareViewModel.setBluetoothEnabled(state == BluetoothAdapter.STATE_ON)
            }
        }
    }

    // ---------- Flashlight (Torch) callback ----------
    private val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
            hardwareViewModel.setFlashlightOn(enabled)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hardwareViewModel = ViewModelProvider(this)[HardwareViewModel::class.java]
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Initial values
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        hardwareViewModel.setWifiEnabled(wifiManager.isWifiEnabled)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        hardwareViewModel.setBluetoothEnabled(bluetoothAdapter?.isEnabled == true)

        if (stepSensor == null) {
            hardwareViewModel.setStepCount(null)
        }

        enableEdgeToEdge()
        setContent {
            MyLabsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "user_profile",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("user_profile") {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())   // ← add this
                            ) {
                                UserProfileScreen()

                                // Button that navigates to View Hardware
                                Button(
                                    onClick = { navController.navigate("view_hardware") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text("View Hardware")
                                }
                            }
                        }

                        composable("view_hardware") {
                            val wifi by hardwareViewModel.wifiState.collectAsStateWithLifecycle()
                            val bt by hardwareViewModel.btState.collectAsStateWithLifecycle()
                            val flash by hardwareViewModel.flashlightState.collectAsStateWithLifecycle()
                            val light by hardwareViewModel.lightValue.collectAsStateWithLifecycle()
                            val accel by hardwareViewModel.accelerometer.collectAsStateWithLifecycle()
                            val steps by hardwareViewModel.stepCount.collectAsStateWithLifecycle()
                            val context = LocalContext.current

                            ViewHardware(
                                wifiState = wifi,
                                btState = bt,
                                fLight = flash,
                                light = light,
                                accel = accel,
                                step = steps,
                                onWifiClick = { openWifiSettings(context) },
                                onBtClick = { openBluetoothSettings(context) },
                                onFlashlightToggle = { enabled ->
                                    setTorchMode(context, enabled)
                                    hardwareViewModel.setFlashlightOn(enabled)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Register sensors
        lightSensor?.let {
            sensorManager.registerListener(lightListener, it, SensorManager.SENSOR_DELAY_UI)
        }
        accelSensor?.let {
            sensorManager.registerListener(accelListener, it, SensorManager.SENSOR_DELAY_UI)
        }
        stepSensor?.let {
            sensorManager.registerListener(stepListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        // Register receivers
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        // Register torch callback
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(torchCallback, null)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(lightListener)
        sensorManager.unregisterListener(accelListener)
        sensorManager.unregisterListener(stepListener)

        try {
            unregisterReceiver(wifiReceiver)
            unregisterReceiver(bluetoothReceiver)
        } catch (_: Exception) {}

        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraManager.unregisterTorchCallback(torchCallback)
    }
}