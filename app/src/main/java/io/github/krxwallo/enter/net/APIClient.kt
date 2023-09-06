package io.github.krxwallo.enter.net

import android.util.Log
import io.github.krxwallo.enter.TAG
import io.github.krxwallo.enter.cacheDoorOpenEndpoint
import io.github.krxwallo.enter.dto.Channel
import io.github.krxwallo.enter.dto.Datapoint
import io.github.krxwallo.enter.dto.Device
import io.github.krxwallo.enter.dto.DeviceList
import io.github.krxwallo.enter.dto.DoorState
import io.github.krxwallo.enter.dto.StateList
import io.github.krxwallo.enter.getDoorOpenEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.simpleframework.xml.core.Persister

object APIClient {
    val scope = CoroutineScope(Dispatchers.IO) + SupervisorJob()

    var ip = "192.168.10.100"
    private val baseUrl get() = "http://$ip/addons/xmlapi"
    private val client = HttpClient(OkHttp) {
        engine {
            config {

            }
        }
    }
    private val serializer = Persister()

    suspend fun getDoors(): List<Device> {
        Log.i(TAG, "getDoors: Getting device list...")
        val response = client.get("$baseUrl/devicelist.cgi") // get devices without states
        val data = response.readBytes()
        Log.i(TAG, "getDoors: Got device list response: ${response.status} (${data.size} bytes)")
        val deviceList = serializer.read(DeviceList::class.java, data.inputStream())
        Log.i(TAG, "getDoors: Devices: ${deviceList.devices?.size ?: -1}")
        /*deviceList.devices?.forEach {
            Log.i(TAG, "getDoors: Device: ${it.name} (${it.address})")
            it.channels?.forEach { channel ->
                Log.i(TAG, "getDoors: Channel: ${channel.name} (${channel.address})")
            }
        }*/

        val doors = deviceList.devices?.filter { it.deviceType == "HmIP-DLD" } ?: emptyList()
        Log.i(TAG, "getDoors: Found ${doors.size} door devices: ${doors.toTypedArray().contentToString()}")
        if (doors.isEmpty()) {
            Log.w(TAG, "getDoors: No doors found!")
        } else if (doors.size > 1) {
            Log.w(TAG, "getDoors: More than one door found!")
        }
        return doors
    }

    private suspend fun getStates(device: Device): List<Channel> {
        Log.i(TAG, "getStates: Getting states for device ${device.name} (${device.iseId})...")

        val response = client.get("$baseUrl/state.cgi?device_id=${device.iseId}")
        val data = response.readBytes()
        Log.i(TAG, "getStates: Got states response: ${response.status} (${data.size} bytes)")
        val stateList = serializer.read(StateList::class.java, data.inputStream())
        val deviceWithStates = stateList.devices?.firstOrNull() ?: run {
            Log.w(TAG, "getStates: No states found for device ${device.name} (${device.iseId})!")
            return emptyList()
        }
        Log.i(TAG, "getStates: States: ${deviceWithStates.channels?.sumOf { it.states?.size ?: 0 } ?: -1}")

        return deviceWithStates.channels ?: emptyList()
    }

    private suspend fun setState(channel: Channel, state: Datapoint, value: String) {
        Log.i(TAG, "setState: Setting state ${state.name} (${state.iseId}) of channel ${channel.name} (${channel.iseId}) to $value...")

        val response = client.get("$baseUrl/statechange.cgi?ise_id=${channel.iseId},${state.iseId}&new_value=$value")
        if (response.status.isSuccess()) {
            Log.i(TAG, "setState: State set successfully: ${response.bodyAsText()}")
        }
    }

    suspend fun setDoorState(door: Device, state: String = DoorState.OPEN) {
        if (door.iseId == null) {
            Log.w(TAG, "openDoor: Door has no ise id!")
            return
        }
        Log.i(TAG, "openDoor: Opening door ${door.name} (${door.iseId})...")

        val cachedOpenEndpoint = getDoorOpenEndpoint(door.iseId!!)
        if (cachedOpenEndpoint != null) {
            Log.i(TAG, "openDoor: Using cached open endpoint: $cachedOpenEndpoint")
            setState(Channel(iseId = cachedOpenEndpoint.channelIseId), Datapoint(iseId = cachedOpenEndpoint.stateIseId), state)
            return
        }


        val stateChannels = getStates(door)

        val lockStateChannel = stateChannels.firstOrNull { it.states?.any { d -> d.type == "LOCK_STATE" } == true } ?: run {
            Log.w(TAG, "openDoor: No lock state channel found for door ${door.name} (${door.iseId})!")
            return
        }

        val lockState = lockStateChannel.states!!.first { it.type == "LOCK_STATE" }

        cacheDoorOpenEndpoint(door.iseId!!, lockStateChannel.iseId!!, lockState.iseId!!)

        setState(lockStateChannel, lockState, "1")
    }
}