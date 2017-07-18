package me.duncanleo.diporto.model

import com.squareup.moshi.Json
import java.util.*

/**
 * Created by duncanleo on 18/7/17.
 */
data class Review (
        val id: Int,
        val rating: Double,
        val time: Date,
        val text: String,
        @Json(name = "user_id") val userId: Int
)
