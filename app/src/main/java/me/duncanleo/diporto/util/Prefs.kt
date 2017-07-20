package me.duncanleo.diporto.util

import android.content.Context

/**
 * Created by duncanleo on 20/7/17.
 */
class Prefs(context: Context) {
    val PREFS_FILENAME = "me.duncanleo.diporto"
    val prefs = context.getSharedPreferences(PREFS_FILENAME, 0)

    val IS_LOGGED_IN = "is_logged_in"

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
}