package me.duncanleo.diporto.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_room.*
import me.duncanleo.diporto.App
import me.duncanleo.diporto.R
import me.duncanleo.diporto.adapter.PlacesRecyclerViewAdapter
import me.duncanleo.diporto.model.Location
import me.duncanleo.diporto.model.Place
import me.duncanleo.diporto.model.Room
import me.duncanleo.diporto.network.Network


class RoomActivity : AppCompatActivity(), OnMapReadyCallback, View.OnTouchListener {
    companion object {
        val roomKey = "room"
    }

    val TAG = "RoomActivity"
    val DIFFY_THRESHOLD_PERCENTAGE = 0.2
    var downY = 0f
    val MAX_SPRING_VALUE = 0.85
    val SINGAPORE = LatLng(1.3521, 103.8198)

    private lateinit var googleMap: GoogleMap
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var room: Room
    private lateinit var spring: Spring
    private val data = mutableListOf<Place>()
    private lateinit var adapter: PlacesRecyclerViewAdapter

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

        placesRecyclerView.layoutManager = LinearLayoutManager(this@RoomActivity)
        placesRecyclerView.addItemDecoration(DividerItemDecoration(this@RoomActivity, DividerItemDecoration.VERTICAL))
        adapter = PlacesRecyclerViewAdapter(data)
        placesRecyclerView.adapter = adapter

        placesRecyclerView.setOnTouchListener(this@RoomActivity)
        placesRecyclerView.addOnItemTouchListener(object: RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                return spring.currentValueIsApproximately(MAX_SPRING_VALUE)
            }
        })

        spring = SpringSystem.create().createSpring()
        spring.addListener(object: SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                placesRecyclerView.translationY = Math.max(-0.02f * placesRecyclerView.height, (spring.currentValue * placesRecyclerView.height).toFloat())
                val alphaValue = Math.min(MAX_SPRING_VALUE, Math.max(0.0, spring.currentValue))
                darkenView.setBackgroundColor(Color.argb(((MAX_SPRING_VALUE - alphaValue) * 255).toInt(), 0, 0, 0))
            }
        })
        spring.springConfig.tension = 300.0

        // Bottom by default
        spring.endValue = MAX_SPRING_VALUE

        Network.getDiportoService().getPlacesByRoomId(room.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ places ->
                    data.addAll(places)
                    placesRecyclerView.adapter.notifyDataSetChanged()
                }, { error ->
                    Log.d(TAG, "Error loading places", error)
                    MaterialDialog.Builder(this@RoomActivity)
                            .title(R.string.label_error)
                            .content(R.string.description_error_loading_places)
                            .show()
                })
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
                    Log.d("RA", "Passing through")
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

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SINGAPORE))

        googleMap.setOnMapLoadedCallback {
            displayLocations()
        }
    }

    fun displayLocations() {
        App.rxLocation?.location()?.lastLocation()
                ?.subscribe ({ userLocation ->
                    // Own marker
                    googleMap.addMarker(MarkerOptions()
                            .position(LatLng(userLocation.latitude, userLocation.longitude))
                            .title(getString(R.string.label_you)))
                    // Other members marker
                    room.members.filter { it.currentLocation != null }.map { googleMap.addMarker(MarkerOptions()
                            .position(it.currentLocation!!.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .title(it.name)
                            .snippet("${"%.2f".format(userLocation.distanceTo(it.currentLocation.getLocation())/1000.0)} km away")) }
                }, { error ->
                    room.members.filter { it.currentLocation != null }.map { googleMap.addMarker(MarkerOptions()
                            .position(it.currentLocation!!.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .title(it.name)) }
                })

        val boundsBuilder = LatLngBounds.builder()
        getLocations().forEach { boundsBuilder.include(it.getLatLng()) }

        val bounds = boundsBuilder.build()

        googleMap.addPolygon(PolygonOptions()
                .add(
                        bounds.northeast,
                        LatLng(bounds.northeast.latitude, bounds.southwest.longitude),
                        bounds.southwest,
                        LatLng(bounds.southwest.latitude, bounds.northeast.longitude)
                )
                .strokeWidth(10f)
                .strokeColor(resources.getColor(R.color.colorLogo)))

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
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
