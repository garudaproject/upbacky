package com.garudaproject.upbacky.util

import kotlinx.serialization.json.Json

object JsonFormat {
  val formatter = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
  }
}
