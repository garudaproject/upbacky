package com.garudaproject.upbacky.data

import kotlinx.serialization.Serializable

@Serializable
data class CallLogs(
    val number: String?,
    val name: String?,
    val type: Int,
    val date: String?,
    val duration: Long
)
