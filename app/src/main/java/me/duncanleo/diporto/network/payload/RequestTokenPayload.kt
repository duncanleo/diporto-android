package me.duncanleo.diporto.network.payload

import com.squareup.moshi.Json

/**
 * Created by duncanleo on 20/7/17.
 */
data class RequestTokenPayload(
        @Json(name = "UserName") val username: String,
        @Json(name = "Password") val password: String
)
