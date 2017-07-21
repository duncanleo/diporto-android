package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Created by duncanleo on 18/7/17.
 */
data class Place (
        val id: Int,
        val name: String,
        val lat: Double,
        val lon: Double,
        val phone: String,
        val address: String,
        @Json(name = "opening_hours") val openingHours: String,
        val categories: List<String>?,
        val photos: List<Photo>,
        val reviews: List<Review>
) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Place> = object : Parcelable.Creator<Place> {
            override fun createFromParcel(source: Parcel): Place = Place(source)
            override fun newArray(size: Int): Array<Place?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readString(),
    source.readDouble(),
    source.readDouble(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.createStringArrayList(),
    ArrayList<Photo>().apply { source.readList(this, Photo::class.java.classLoader) },
    source.createTypedArrayList(Review.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeDouble(lat)
        dest.writeDouble(lon)
        dest.writeString(phone)
        dest.writeString(address)
        dest.writeString(openingHours)
        dest.writeStringList(categories)
        dest.writeList(photos)
        dest.writeTypedList(reviews)
    }
}
