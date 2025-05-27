package com.garudaproject.upbacky.data

import kotlinx.serialization.Serializable

@Serializable
data class AppsList(
    val appName: String,
    val packageName: String,
    val versionName: String?,
    val versionCode: Long,
    val firstInstallTime: String?,
    val lastUpdateTime: String?,
    val sourceDir: String,
    val dataDir: String,
    val isSystemApp: Boolean,
    val sourceHash: String
)
