package com.garudaproject.upbacky.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.garudaproject.upbacky.data.AppsList
import com.garudaproject.upbacky.util.DateFormat
import com.garudaproject.upbacky.util.Hash
import com.garudaproject.upbacky.util.JsonFormat
import java.io.File

class AppsListReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    try {
      val contacts = getAppLists(context)
      val file = File(context.getExternalFilesDir(null), "apps.txt")
      file.writeText(contacts)
      Log.d("AppsListReceiver", "Saved to: ${file.absolutePath}")
    } catch (e: Exception) {
      Log.e("AppsListReceiver", "Failed to export app lists: ${e.message}", e)
    }
  }

  private fun getAppLists(context: Context): String {
    val packageManager = context.packageManager
    val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    val apps =
        packages.mapNotNull { appInfo ->
          try {
            val pkgInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
            AppsList(
                appName = packageManager.getApplicationLabel(appInfo).toString(),
                packageName = appInfo.packageName,
                versionName = pkgInfo.versionName,
                versionCode =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                      pkgInfo.longVersionCode
                    } else {
                      @Suppress("DEPRECATION") pkgInfo.versionCode.toLong()
                    },
                firstInstallTime =
                    DateFormat.formatter.format(java.util.Date(pkgInfo.firstInstallTime)),
                lastUpdateTime =
                    DateFormat.formatter.format(java.util.Date(pkgInfo.lastUpdateTime)),
                sourceDir = appInfo.sourceDir ?: "",
                dataDir = appInfo.dataDir ?: "",
                isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                sourceHash = Hash.getFileHash(appInfo.sourceDir ?: ""))
          } catch (e: Exception) {
            null
          }
        }

    return JsonFormat.formatter.encodeToString(apps)
  }
}
