package me.duncanleo.diporto.network

import android.util.Log
import me.duncanleo.diporto.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by duncanleo on 11/7/17.
 */
object Network {
    fun getDiportoService(): DiportoService {
        return getRetrofitBuilder().baseUrl("")
                .build()
                .create(DiportoService::class.java)
    }

    fun getRetrofitBuilder(): Retrofit.Builder {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("Retrofit", message) }
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor({ chain ->
            // Add the User Agent header
            val request = chain.request().newBuilder()
//                    .addHeader("User-Agent", USER_AGENT)
                    .build()
            chain.proceed(request)
        }).build()
        return Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(MoshiConverterFactory.create()).client(client)
    }
}