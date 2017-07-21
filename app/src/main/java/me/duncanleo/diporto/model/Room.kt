package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Created by duncanleo on 17/7/17.
 */
data class Room (
        val id: Int,
        val name: String,
        val owner: User,
        val members: List<User>,
        @Json(name = "short_code") val shortCode: String?
) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Room> = object : Parcelable.Creator<Room> {
            override fun createFromParcel(source: Parcel): Room = Room(source)
            override fun newArray(size: Int): Array<Room?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readString(),
    source.readParcelable<User>(User::class.java.classLoader),
    source.createTypedArrayList(User.CREATOR),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeParcelable(owner, 0)
        dest.writeTypedList(members)
        dest.writeString(shortCode)
    }
}
