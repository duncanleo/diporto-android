package me.duncanleo.diporto.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_room.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.adapter.PlacesRecyclerViewAdapter
import me.duncanleo.diporto.model.Location
import me.duncanleo.diporto.model.Place
import me.duncanleo.diporto.model.Review
import me.duncanleo.diporto.model.Room
import java.util.*


class RoomActivity : AppCompatActivity(), OnMapReadyCallback, View.OnTouchListener {
    companion object {
        val roomKey = "room"
    }

    val DIFFY_THRESHOLD_PERCENTAGE = 0.2
    var downY = 0f
    val MAX_SPRING_VALUE = 0.85

    private lateinit var googleMap: GoogleMap
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var room: Room
    private lateinit var spring: Spring

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.onCreate(mapViewBundle)

        mapView.getMapAsync(this@RoomActivity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        room = intent.getParcelableExtra<Room>(roomKey)
        supportActionBar?.title = room.name

        val fakePlaces = MutableList(20) {
            Place(-1, "Pink Candy $it", 1.0, 103.0, "+65 12345678", "123 ABC Street", "", listOf("food"), listOf(), listOf(Review(-1, 5.0, Date(), "It was really good!!!!", -1)))
        }
        placesRecyclerView.layoutManager = LinearLayoutManager(this@RoomActivity)
        placesRecyclerView.addItemDecoration(DividerItemDecoration(this@RoomActivity, DividerItemDecoration.VERTICAL))
        placesRecyclerView.adapter = PlacesRecyclerViewAdapter(fakePlaces)

        placesRecyclerView.setOnTouchListener(this@RoomActivity)

        spring = SpringSystem.create().createSpring()
        spring.addListener(object: SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                placesRecyclerView.translationY = Math.max(-0.02f * placesRecyclerView.height, (spring.currentValue * placesRecyclerView.height).toFloat())
                val alphaValue = Math.min(MAX_SPRING_VALUE, Math.max(0.0, spring.currentValue))
                darkenView.setBackgroundColor(Color.argb(((MAX_SPRING_VALUE - alphaValue) * 255).toInt(), 0, 0, 0))
                Log.d("RA", "spring curv = ${spring.currentValue}")
            }
        })
        spring.springConfig.tension = 300.0

        // Bottom by default
        spring.endValue = MAX_SPRING_VALUE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState?.get(MAPVIEW_BUNDLE_KEY) as? Bundle
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState?.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    var temp = 0f

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        val diffY = event.rawY - downY
        val verticalScrollOffset = placesRecyclerView.computeVerticalScrollOffset()
        val translationY = placesRecyclerView.translationY

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.rawY
                temp = placesRecyclerView.translationY
                return true
            }
            MotionEvent.ACTION_UP -> {
                val percentage = translationY / placesRecyclerView.height
                if (percentage > DIFFY_THRESHOLD_PERCENTAGE && diffY > 0) {
                    // bounce down
                    spring.endValue = MAX_SPRING_VALUE
                } else {
                    // bounce return to top
                    spring.endValue = 0.0
                }
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                if ((diffY < 0f && translationY <= 0f) || (diffY > 0f && verticalScrollOffset != 0)) {
                    return false
                }
                placesRecyclerView.translationY = temp + diffY
                spring.currentValue = (placesRecyclerView.translationY / placesRecyclerView.height).toDouble()
                return true
            }
        }
        return false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Add a marker in Sydney and move the camera
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        displayLocations()
    }

    fun displayLocations() {
        room.members.filter { it.currentLocation != null }.map { googleMap.addMarker(MarkerOptions()
                .position(it.currentLocation!!.getLatLng())
                .title(it.name)) }
        googleMap.addPolyline(PolylineOptions()
                .add(*getLocations().map { LatLng(it.lat, it.lon) }.toTypedArray())
                .width(5f)
                .color(Color.GREEN))
    }

    fun getLocations(): List<Location> {
        return room.members.map { it.currentLocation }.filterNotNull()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
