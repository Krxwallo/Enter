package io.github.krxwallo.enter.dto

data class OpenEndpoint(
    /** Ise ID of lock state channel */
    val channelIseId: String,
    /** Ise ID of lock state */
    val stateIseId: String,
)