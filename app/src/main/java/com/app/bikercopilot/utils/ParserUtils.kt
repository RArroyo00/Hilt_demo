package com.app.bikercopilot.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun <T : Any> deserialize(clazz: Class<T>, b: String): T? {
    return getGson().fromJson(b, clazz)
}

fun <T : Any> serialize(data: T): String {
    return getGson().toJson(data)
}

private fun getGson(): Gson {
   return GsonBuilder().disableHtmlEscaping().create()
}