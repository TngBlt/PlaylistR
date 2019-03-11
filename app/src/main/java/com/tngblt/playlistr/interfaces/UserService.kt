package com.tngblt.playlistr.interfaces

import com.tngblt.playlistr.models.spotifyData.user.User
import com.tngblt.playlistr.utils.BASE_URL
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface UserService {

    @GET("me")
    fun getCurrentUserData(@Header("Authorization") authHeader:String): Observable<User>

    @GET("users/{user_id}")
    fun getUserData(@Path("user_id") userId:String, @Header("Authorization") authHeader:String): Observable<User>

    companion object {

        fun create(): UserService {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client:OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(50,TimeUnit.MILLISECONDS)
                .readTimeout(500,TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return retrofit.create(UserService::class.java)
        }
    }
}