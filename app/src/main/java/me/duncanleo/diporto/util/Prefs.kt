package me.duncanleo.diporto.util

import android.content.Context

/**
 * Created by duncanleo on 20/7/17.
 */
class Prefs(context: Context) {
    val PREFS_FILENAME = "me.duncanleo.diporto"
    val prefs = context.getSharedPreferences(PREFS_FILENAME, 0)

    val IS_ONBOARDED = "is_onboarded"
    val ACCESS_TOKEN = "access_token"
    val REFRESH_TOKEN = "refresh_token"

    var isOnboarded
        get() = prefs.getBoolean(IS_ONBOARDED, false)
        set(value) = prefs.edit().putBoolean(IS_ONBOARDED, value).apply()

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(REFRESH_TOKEN, value).apply()
}