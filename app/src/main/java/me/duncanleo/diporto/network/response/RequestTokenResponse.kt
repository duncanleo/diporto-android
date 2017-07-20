package me.duncanleo.diporto.network.response

import java.util.*

/**
 * Created by duncanleo on 20/7/17.
 */
data class RequestTokenResponse (
        val token: String,
        val expiration: Date
)
