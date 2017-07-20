package me.duncanleo.diporto

import android.app.Application
import com.patloew.rxlocation.RxLocation
import me.duncanleo.diporto.util.Prefs

/**
 * Created by duncanleo on 20/7/17.
 */
val prefs: Prefs by lazy {
    App.prefs!!
}

val rxLocation: RxLocation by lazy {
    App.rxLocation!!
}

class App : Application() {
    companion object {
        var prefs: Prefs? = null
        var rxLocation: RxLocation? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        rxLocation = RxLocation(applicationContext)
        super.onCreate()
    }
}
