package com.garudaproject.upbacky.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.garudaproject.upbacky.data.Contact
import com.garudaproject.upbacky.util.JsonFormat
import java.io.File

class ContactReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Log.d("ContactReceiver", "Broadcast received.")

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) !=
        PackageManager.PERMISSION_GRANTED) {
      Log.e("ContactReceiver", "READ_CONTACTS permission not granted.")
      return
    }

    try {
      val contacts = getContacts(context)
      val file = File(context.getExternalFilesDir(null), "contacts.txt")
      file.writeText(contacts)
      Log.d("ContactReceiver", "Saved to: ${file.absolutePath}")
    } catch (e: Exception) {
      Log.e("ContactReceiver", "Failed to export contacts: ${e.message}", e)
    }
  }

  private fun getContacts(context: Context): String {
    val contactList = mutableListOf<Contact>()
    val cursor =
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

    cursor?.use {
      val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
      val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

      while (it.moveToNext()) {
        val name = it.getString(nameIndex) ?: "Unknown"
        val number = it.getString(numberIndex) ?: "Unknown"
        contactList.add(Contact(name, number))
      }
    }

    return JsonFormat.formatter.encodeToString(contactList)
  }
}
