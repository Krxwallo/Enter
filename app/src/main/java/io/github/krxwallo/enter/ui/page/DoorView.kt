package io.github.krxwallo.enter.ui.page

import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import io.github.krxwallo.enter.TAG
import io.github.krxwallo.enter.clearDoorOpenEndpoint
import io.github.krxwallo.enter.defaultDoorId
import io.github.krxwallo.enter.dto.Device
import io.github.krxwallo.enter.dto.DoorState
import io.github.krxwallo.enter.getDoorOpenEndpoint
import io.github.krxwallo.enter.net.APIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ColumnScope.DoorView() {
    var fetchingDoors by remember {
        mutableStateOf(false)
    }
    var fetchingError by remember {
        mutableStateOf<Throwable?>(null)
    }
    var actionError by remember {
        mutableStateOf<Throwable?>(null)
    }
    var newDoorState by remember {
        mutableStateOf<Pair<Device, String>?>(null)
    }
    var doors by remember {
        mutableStateOf(emptyList<Device>())
    }
    // Fetch doors
    LaunchedEffect(key1 = fetchingDoors) {
        if (fetchingDoors) {
            launch(Dispatchers.IO) {
                runCatching {
                    doors = APIClient.getDoors()
                }.onFailure { fetchingError = it }
                fetchingDoors = false
            }
        }
    }

    // Open door
    LaunchedEffect(key1 = newDoorState) {
        if (newDoorState != null) {
            actionError = null
            launch(Dispatchers.IO) {
                runCatching {
                    APIClient.setDoorState(newDoorState!!.first, newDoorState!!.second)
                }.onFailure { actionError = it }
                newDoorState = null
            }
        }
    }

    Button({
        doors = emptyList()
        fetchingError = null
        fetchingDoors = true
    }, Modifier.align(Alignment.CenterHorizontally), enabled = !fetchingDoors) {
        Text("Fetch doors!")
    }

    Text("Doors:", modifier = Modifier.padding(top = 16.dp), fontSize = MaterialTheme.typography.headlineMedium.fontSize)
    if (fetchingError != null) {
        Text("Error fetching doors: $fetchingError", color = MaterialTheme.colorScheme.error)
    }
    else if (doors.isEmpty()) {
        if (fetchingDoors) Text("Fetching...") else Text("No doors found! Try fetching.")
    }
    doors.forEach {
        var isDefault by remember {
            mutableStateOf(false)
        }
        var isCached by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true) {
            while (true) {
                isDefault = it.iseId != null && it.iseId == defaultDoorId
                isCached = it.iseId != null && getDoorOpenEndpoint(it.iseId ?: "") != null
                delay(0.5.seconds)
            }
        }
        Text(buildAnnotatedString {
            append(it.name ?: "Unnamed")
            append(" (")
            append(it.iseId ?: "No id!")
            append(")")
            if (isDefault) {
                append(" (DEFAULT)")
            }
        }, modifier = Modifier.padding(top = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                newDoorState = it to DoorState.OPEN
            }, enabled = it.iseId != null) {
                Text("Open")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                newDoorState = it to DoorState.UNLOCKED
            }, enabled = it.iseId != null) {
                Text("Unlock")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    newDoorState = it to DoorState.LOCKED
                },
                enabled = it.iseId != null,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Lock")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                defaultDoorId = it.iseId
                Log.i(TAG, "HomePage: default id: $defaultDoorId")
                Log.i(TAG, "HomePage: is default: $isDefault")
            }, enabled = !isDefault && it.iseId != null) {
                Text("Set Default")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                clearDoorOpenEndpoint(it.iseId ?: "")
            }, enabled = isCached) {
                Text("Clear Cache")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}