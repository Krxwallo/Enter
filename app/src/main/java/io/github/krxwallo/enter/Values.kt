package io.github.krxwallo.enter

import android.content.SharedPreferences
import io.github.krxwallo.enter.dto.OpenEndpoint

var preferences: SharedPreferences? = null

var defaultDoorId: String? set(value) {
    val editor = preferences?.edit() ?: return
    editor.putString("default_door_id", value)
    editor.apply()
} get() = preferences?.getString("default_door_id", null)

fun cacheDoorOpenEndpoint(doorId: String, channelIseId: String, stateIseId: String) {
    val editor = preferences?.edit() ?: return
    editor.putString("door_open_endpoint_channel_ise_id_$doorId", channelIseId)
    editor.putString("door_open_endpoint_state_ise_id_$doorId", stateIseId)
    editor.apply()
}

fun getDoorOpenEndpoint(doorId: String): OpenEndpoint? {
    val channelIseId = preferences?.getString("door_open_endpoint_channel_ise_id_$doorId", null) ?: return null
    val stateIseId = preferences?.getString("door_open_endpoint_state_ise_id_$doorId", null) ?: return null
    return OpenEndpoint(channelIseId, stateIseId)
}

fun clearDoorOpenEndpoint(doorId: String) {
    val editor = preferences?.edit() ?: return
    editor.remove("door_open_endpoint_channel_ise_id_$doorId")
    editor.remove("door_open_endpoint_state_ise_id_$doorId")
    editor.apply()
}

fun setCachedIp(ip: String) {
    val editor = preferences?.edit() ?: return
    editor.putString("homematic_ip", ip)
    editor.apply()
}

fun getCachedIp(): String? {
    return preferences?.getString("homematic_ip", null)
}