package com.sobi.replantation.domain

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sobi.replantation.domain.model.TokenData

class PreferenceService(val context: Context) {
    val pref by lazy { context.getSharedPreferences("sobi-replantation", Context.MODE_PRIVATE) }
    val mapper by lazy { jacksonObjectMapper() }
    var token: TokenData?
    get() {
        val data = pref.getString("token_data", null) ?: return null
        return mapper.readValue(data, TokenData::class.java)
    }
    set(value) {
        if (value !=null){
            val json = mapper.writeValueAsString(value)
            pref.edit().putString("token_data", json).apply()
        }
    }

    //user account data
    var accessId: Int
        get() = pref.getInt("access_id", -1)
        set(value) {
            pref.edit().putInt("access_id", value).apply()
        }
    var username: String?
        get() = pref.getString("uname", "") ?: ""
        set(value) {
            pref.edit().putString("uname", value).apply()
        }
    var firstName: String?
        get() = pref.getString("fname", "") ?: ""
        set(value) {
            pref.edit().putString("fname", value).apply()
        }
    var email: String
        get() = pref.getString("email", "") ?: ""
        set(value) {
            pref.edit().putString("email", value).apply()
        }
    var lastName: String?
        get() = pref.getString("lname", "") ?: ""
        set(value) {
            pref.edit().putString("lname", value).apply()
        }
    var koperasiId: Int
        get() = pref.getInt("kopid", -1)
        set(value) {
            pref.edit().putInt("kopid", value).apply()
        }
    var deviceId: String?
        get() = pref.getString("deviceid", "") ?: ""
        set(value) {
            pref.edit().putString("deviceid", value).apply()
        }
}