package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Created by duncanleo on 17/7/17.
 */
data class User (
        val name: String,
        val email: String?,
        @Json(name = "current_location") val currentLocation: Location?
) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readParcelable<Location>(Location::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(email)
        dest.writeParcelable(currentLocation, 0)
    }
}
