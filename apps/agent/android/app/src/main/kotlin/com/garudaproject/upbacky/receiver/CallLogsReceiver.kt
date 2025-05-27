package com.garudaproject.upbacky.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.CallLog
import android.util.Log
import androidx.core.content.ContextCompat
import com.garudaproject.upbacky.data.CallLogs
import com.garudaproject.upbacky.util.DateFormat
import com.garudaproject.upbacky.util.JsonFormat
import java.io.File

class CallLogsReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Log.d("CallLogsReceiver", "Broadcast received.")

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALL_LOG) !=
        PackageManager.PERMISSION_GRANTED) {
      Log.e("CallLogsReceiver", "READ_CALL_LOG permission not granted.")
      return
    }

    try {
      val callLogs = getCallLogs(context)
      val file = File(context.getExternalFilesDir(null), "call_logs.txt")
      file.writeText(callLogs)
      Log.d("CallLogsReceiver", "Saved to: ${file.absolutePath}")
    } catch (e: Exception) {
      Log.e("CallLogsReceiver", "Failed to export call logs: ${e.message}", e)
    }
  }

  private fun getCallLogs(context: Context): String {
    val callLogs = mutableListOf<CallLogs>()

    val projection =
        arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION)

    val cursor =
        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI, projection, null, null, "${CallLog.Calls.DATE} DESC")

    cursor?.use {
      val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
      val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
      val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
      val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
      val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

      while (it.moveToNext()) {
        val entry =
            CallLogs(
                number = it.getString(numberIndex),
                name = it.getString(nameIndex),
                type = it.getInt(typeIndex),
                date = DateFormat.formatter.format(java.util.Date(it.getLong(dateIndex))),
                duration = it.getLong(durationIndex))
        callLogs.add(entry)
      }
    }

    return JsonFormat.formatter.encodeToString(callLogs)
  }
}
