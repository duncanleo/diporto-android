package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by duncanleo on 18/7/17.
 */
data class Review (
        val id: Int,
        val rating: Double,
        val time: Date,
        val text: String,
        val user: User
) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Review> = object : Parcelable.Creator<Review> {
            override fun createFromParcel(source: Parcel): Review = Review(source)
            override fun newArray(size: Int): Array<Review?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readDouble(),
    source.readSerializable() as Date,
    source.readString(),
    source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeDouble(rating)
        dest.writeSerializable(time)
        dest.writeString(text)
        dest.writeParcelable(user, 0)
    }
}
