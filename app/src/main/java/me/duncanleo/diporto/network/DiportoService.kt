package me.duncanleo.diporto.network

import io.reactivex.Single
import me.duncanleo.diporto.model.Place
import me.duncanleo.diporto.model.Room
import okhttp3.Response
import retrofit2.http.*

/**
 * Created by duncanleo on 11/7/17.
 */
interface DiportoService {
    @GET("places")
    fun getPlacesByRoomId(@Query("roomId") roomId: Int): Single<List<Place>>

    @GET("rooms")
    fun getRooms(): Single<List<Room>>

    @POST("login")
    @FormUrlEncoded
    fun login(@Field("UserName") userName: String, @Field("Password") password: String) : Single<Response>
}
