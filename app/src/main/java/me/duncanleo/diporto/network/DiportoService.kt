package me.duncanleo.diporto.network

import io.reactivex.Single
import me.duncanleo.diporto.model.Place
import me.duncanleo.diporto.model.Room
import me.duncanleo.diporto.network.payload.CreateRoomPayload
import me.duncanleo.diporto.network.payload.RequestTokenPayload
import me.duncanleo.diporto.network.response.RequestTokenResponse
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by duncanleo on 11/7/17.
 */
interface DiportoService {
    @GET("places")
    fun getPlacesByRoomId(@Query("roomId") roomId: Int): Single<List<Place>>

    @GET("rooms")
    fun getRooms(): Single<List<Room>>

    @POST("token")
    fun requestToken(@Body payload: RequestTokenPayload): Single<RequestTokenResponse>

    @POST("token")
    fun refreshToken(@Body payload: RequestTokenPayload): Call<RequestTokenResponse>

    @POST("rooms")
    fun createRoom(@Body payload: CreateRoomPayload): Single<Any>

    @POST("rooms/{shortCode}/memberships")
    fun joinRoom(@Path("shortCode") shortCode: String): Single<Any>

    @POST("login")
    @FormUrlEncoded
    fun login(@Field("UserName") userName: String, @Field("Password") password: String) : Single<Response>
}
