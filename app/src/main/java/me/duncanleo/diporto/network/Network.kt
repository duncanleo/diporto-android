package me.duncanleo.diporto.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Rfc3339DateJsonAdapter
import me.duncanleo.diporto.BuildConfig
import me.duncanleo.diporto.prefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Date
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by duncanleo on 11/7/17.
 */
object Network {
    var userAgent = "Diporto Android"

    val baseURL = "https://diporto-mobile.undertide.co/api/"
    fun getDiportoService(): DiportoService {
        return getRetrofitBuilder().baseUrl(baseURL)
                .build()
                .create(DiportoService::class.java)
    }

    val moshiConverter
        get() = {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .build()
            MoshiConverterFactory.create(moshi)
        }

    fun getRetrofitBuilder(): Retrofit.Builder {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("Retrofit", message) }
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor({ chain ->
                    // Add the User Agent header
                    val request = chain.request().newBuilder()
                            .addHeader("User-Agent", userAgent)
                            .build()
                    chain.proceed(request)
                })
                .addInterceptor { chain ->
                    if (!prefs.accessToken.isNullOrEmpty()) {
                        chain.proceed(chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer ${prefs.accessToken}")
                                .build())
                    } else {
                        chain.proceed(chain.request())
                    }
                }
                .authenticator(TokenAuthenticator())
                .build()
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(moshiConverter())
                .client(client)
    }
}