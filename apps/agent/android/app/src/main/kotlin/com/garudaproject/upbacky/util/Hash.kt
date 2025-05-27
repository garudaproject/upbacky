package com.garudaproject.upbacky.util

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class Hash {
  companion object {
    fun getFileHash(path: String, algorithm: String = "SHA-256"): String {
      val file = File(path)
      if (!file.exists()) return "File not found"

      return try {
        val digest = MessageDigest.getInstance(algorithm)
        val buffer = ByteArray(8192)
        FileInputStream(file).use { fis ->
          var bytesRead: Int
          while (fis.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
          }
        }
        digest.digest().joinToString("") { "%02x".format(it) }
      } catch (e: Exception) {
        "Error: ${e.message}"
      }
    }
  }
}
