package com.easylife.composeplayground

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs


@Composable
fun ChargingCurrentScreen() {
    val context = LocalContext.current
    var chargingCurrent by remember { mutableStateOf(0) }
    var isCharging by remember { mutableStateOf(false) }

    // LaunchedEffect to update the charging current periodically
    LaunchedEffect(Unit) {
        while (true) {
            chargingCurrent = getChargingCurrent(context)
            isCharging = isDeviceCharging(context)
            delay(1000) // Update every second
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCharging) {
            Text(text = "Charging Current: ${abs(chargingCurrent)} mA")
        } else {
            Text(text = "Discharging Current:- ${chargingCurrent} mA")
        }
    }
}

fun getChargingCurrent(context: Context): Int {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000 // Convert to mA
}

fun isDeviceCharging(context: Context): Boolean {
    val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
}

@Composable
fun ChargingCurrentScreen3() {
    val context = LocalContext.current
    var chargingCurrent by remember { mutableStateOf(0) }
    var isCharging by remember { mutableStateOf(false) }

    // LaunchedEffect to update the charging current periodically
    LaunchedEffect(Unit) {
        while (true) {
            chargingCurrent = getChargingCurrent(context)
            isCharging = chargingCurrent > 0 // Positive current means charging
            delay(1000) // Update every second
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCharging) {
            Text(text = "Charging Current: ${abs(chargingCurrent)} mA")
        } else {
            Text(text = "Discharging Current: ${abs(chargingCurrent)} mA")
        }
    }
}

fun getChargingCurrent3(context: Context): Int {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000 // Convert to mA
}