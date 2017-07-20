package me.duncanleo.diporto.network.response

import com.squareup.moshi.Json
import java.util.*


/**
 * Created by duncanleo on 20/7/17.
 */
data class RequestTokenResponse (
        val token: String,
        @Json(name = "refresh_token") val refreshToken: String,
        val expiration: Date
)
