package me.duncanleo.diporto.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by duncanleo on 18/7/17.
 */
data class Photo (
        val id: Int
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(source: Parcel): Photo = Photo(source)
            override fun newArray(size: Int): Array<Photo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
    }
}
