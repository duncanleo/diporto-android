package me.duncanleo.diporto.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place.*
import kotlinx.android.synthetic.main.content_place.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.model.Place
import me.duncanleo.diporto.network.Network

class PlaceActivity : AppCompatActivity() {
    companion object {
        val placeKey = "placeKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val place = intent.getParcelableExtra<Place>(placeKey)
        title = place.name

        if (place.photos.any()) {
            appBarImageCarousel.setImageListener { position, imageView ->
                Picasso.with(this@PlaceActivity).load("${Network.baseURL}photos/${place.photos[position].id}").into(imageView)
            }
            appBarImageCarousel.pageCount = place.photos.size
        }

        addressTextView.text = place.address
        phoneTextView.text = place.phone


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { onBackPressed() }
        }
        return super.onOptionsItemSelected(item)
    }
}
