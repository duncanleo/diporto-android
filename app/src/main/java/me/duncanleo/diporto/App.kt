package me.duncanleo.diporto

import android.app.Application
import me.duncanleo.diporto.util.Prefs

/**
 * Created by duncanleo on 20/7/17.
 */
val prefs: Prefs by lazy {
    App.prefs!!
}

class App : Application() {
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}
