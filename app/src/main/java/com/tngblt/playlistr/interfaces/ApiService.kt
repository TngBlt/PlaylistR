package com.tngblt.playlistr.interfaces

import com.tngblt.playlistr.models.spotifyData.ListPlaylists
import com.tngblt.playlistr.models.spotifyData.playlist.GeneralPlaylistInfo
import com.tngblt.playlistr.models.spotifyData.search.GeneralSearchInfo
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
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("me")
    fun getCurrentUserData(@Header("Authorization") authHeader:String): Observable<User>

    @GET("users/{user_id}")
    fun getUserData(@Path("user_id") userId:String, @Header("Authorization") authHeader:String): Observable<User>

    @GET("me/playlists")
    fun getCurrentUserPlaylists(@Header("Authorization") authHeader: String):Observable<GeneralPlaylistInfo>

    @GET("users/{user_id}/playlists")
    fun getUserPlaylists(@Path("user_id") userId:String, @Header("Authorization") authHeader:String): Observable<GeneralPlaylistInfo>

    @GET("search")
    fun getResearchResult(@Query("q") search:String, @Query("type") type:String = "album,artist,playlist,track", @Header("Authorization") authHeader: String): Observable<GeneralSearchInfo>

    @GET("search")
    fun getResearchPlaylistResult(@Query("q") search:String, @Query("type") type:String = "playlist", @Header("Authorization") authHeader: String): Observable<GeneralSearchInfo>


    companion object {

        fun create(): ApiService {

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

            return retrofit.create(ApiService::class.java)
        }
    }
}