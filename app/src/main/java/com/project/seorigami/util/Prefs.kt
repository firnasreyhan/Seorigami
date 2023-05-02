package com.project.seorigami.util

import android.content.Context
import com.google.gson.Gson
import com.project.seorigami.model.response.UserResponseModel

class Prefs(context: Context) {
    private val KEY_JWT = "KEY_JWT"
    private val KEY_USER = "KEY_USER"

    private val preferencesJWT = context.getSharedPreferences(KEY_JWT, Context.MODE_PRIVATE)
    private val preferencesUser = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)

    var jwt: String?
        get() = preferencesJWT.getString(KEY_JWT, null)
        set(value) = preferencesJWT.edit().putString(KEY_JWT, value).apply()

    var user: UserResponseModel?
        get() = Gson().fromJson(preferencesUser.getString(KEY_USER, null), UserResponseModel::class.java)
        set(value) = preferencesUser.edit().putString(KEY_USER, Gson().toJson(value)).apply()
}