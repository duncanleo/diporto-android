package me.duncanleo.diporto.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import me.duncanleo.diporto.R

/**
 * Created by duncanleo on 18/7/17.
 */
class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showSkipButton(false)

        askForPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
        ), 2)

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.label_welcome),
                getString(R.string.description_welcome),
                R.drawable.ic_logo,
                resources.getColor(R.color.colorLogo)
        ))

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.label_location),
                getString(R.string.description_location),
                R.drawable.ic_location_on_72dp,
                resources.getColor(R.color.colorBrand)
        ))

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.label_done),
                getString(R.string.description_done),
                R.drawable.ic_done_72dp,
                resources.getColor(R.color.colorBrand2)
        ))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }
}