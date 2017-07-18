package me.duncanleo.diporto.model

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
)
