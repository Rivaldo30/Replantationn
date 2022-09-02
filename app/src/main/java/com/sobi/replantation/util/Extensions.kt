package com.appsobi.pendataan.util

import android.util.Base64
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

inline fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    md.update(toByteArray())
    val hash = BigInteger(1, md.digest())
    val result = hash.toString(16)
    return if (result.length % 2 != 0) {
        "0$result"
    } else result
}

inline fun File.base64(): String {
    val bytes = this.readBytes()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}