package me.duncanleo.diporto.network

import me.duncanleo.diporto.network.payload.RequestTokenPayload
import me.duncanleo.diporto.prefs
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Created by duncanleo on 20/7/17.
 */
class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response?): Request {
        val refreshTokenCall = Network.getDiportoService().refreshToken(RequestTokenPayload(
                username = null,
                password = null,
                refreshToken = prefs.refreshToken,
                grantType = "refresh_token"
        )).execute()
        prefs.accessToken = refreshTokenCall.body().token
        prefs.refreshToken = refreshTokenCall.body().refreshToken
        return response?.request()!!
                .newBuilder()
                .header("Authorization", "Bearer ${refreshTokenCall.body().token}")
                .build()
    }
}