package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json

/**
 * Created by duncanleo on 18/7/17.
 */
data class Location (
        @Json(name = "y") val lat: Double,
        @Json(name = "x") val lon: Double
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Location> = object : Parcelable.Creator<Location> {
            override fun createFromParcel(source: Parcel): Location = Location(source)
            override fun newArray(size: Int): Array<Location?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readDouble(),
    source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(lat)
        dest.writeDouble(lon)
    }

    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }

    fun getLocation(): android.location.Location {
        val loc = android.location.Location("dummyProvider")
        loc.latitude = lat
        loc.longitude = lon
        return loc
    }
}
