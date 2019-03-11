package com.tngblt.playlistr.network

import com.tngblt.playlistr.interfaces.UserService
import com.tngblt.playlistr.models.spotifyData.user.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {
    @GET("me")
    fun getCurrentUserData(@Header("Authorization") authHeader:String): Observable<User>

    @GET("users/{user_id}")
    fun getUserData(@Path("user_id") userId:String, @Header("Authorization") authHeader:String): Observable<User>
}