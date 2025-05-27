package com.garudaproject.upbacky.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Telephony
import android.util.Log
import androidx.core.content.ContextCompat
import com.garudaproject.upbacky.data.SMS
import com.garudaproject.upbacky.util.DateFormat
import com.garudaproject.upbacky.util.JsonFormat
import java.io.File

class SMSReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Log.d("SMSReceiver", "Broadcast received.")

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) !=
        PackageManager.PERMISSION_GRANTED) {
      Log.e("SMSReceiver", "READ_SMS permission not granted.")
      return
    }

    try {
      val contacts = getSMSs(context)
      val file = File(context.getExternalFilesDir(null), "sms.txt")
      file.writeText(contacts)
      Log.d("SMSReceiver", "Saved to: ${file.absolutePath}")
    } catch (e: Exception) {
      Log.e("SMSReceiver", "Failed to export sms: ${e.message}", e)
    }
  }

  private fun getSMSs(context: Context): String {
    val smsList = mutableListOf<SMS>()
    val smsUri = Uri.parse("content://sms")
    val cursor = context.contentResolver.query(smsUri, null, null, null, null)

    cursor?.use {
      val addressIndex = it.getColumnIndexOrThrow("address")
      val bodyIndex = it.getColumnIndexOrThrow("body")
      val dateIndex = it.getColumnIndexOrThrow("date")
      val typeIndex = it.getColumnIndexOrThrow("type")

      while (it.moveToNext()) {
        val address = it.getString(addressIndex)
        val body = it.getString(bodyIndex)
        val dateMillis = it.getLong(dateIndex)
        val date = DateFormat.formatter.format(java.util.Date(dateMillis))
        val type = it.getInt(typeIndex)
        val typeStr =
            when (type) {
              Telephony.Sms.MESSAGE_TYPE_INBOX -> "INBOX"
              Telephony.Sms.MESSAGE_TYPE_SENT -> "SENT"
              else -> "OTHER"
            }

        smsList.add(SMS(typeStr, address, date, body))
      }
    }

    return JsonFormat.formatter.encodeToString(smsList)
  }
}
