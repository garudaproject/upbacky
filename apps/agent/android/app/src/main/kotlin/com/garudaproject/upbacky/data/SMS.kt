package com.garudaproject.upbacky.data

import kotlinx.serialization.Serializable

@Serializable
data class SMS(val type: String?, val address: String?, val date: String?, val body: String?)
